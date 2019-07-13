package com.example.imdb.Activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.example.imdb.Fragment.GridRecyclerFragment;
import com.example.imdb.Model.Movie;
import com.example.imdb.Model.SearchResult;
import com.example.imdb.R;
import com.example.imdb.Retrofit.ApiCall;
import com.example.imdb.Utility.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Movie> movies;
    public GridRecyclerFragment gridFragment;
    public SearchResult searchResult;
    public final String TAG="MainActivity";

    public ProgressDialog progressDialog;
    public Toolbar toolbar;
    public ViewPager viewPager;
    public TabLayout tabLayout;
    public SearchView searchView;


    public boolean reachEnd;
    public String latestQuery;
    public int loadedPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();

        gridFragment = new GridRecyclerFragment();

        setUpLazyLoad();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching...");
        progressDialog.setCancelable(false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    public void setUpLazyLoad(){
        gridFragment.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!reachEnd) {
                    movies.add(null);
                    gridFragment.gridRecyclerAdapter.notifyItemInserted(movies.size() - 1);
                    getData(latestQuery, false);
                }
                Log.i("setOnLoadMoreListener","Called");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {//must
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));


        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MainActivity.this.latestQuery = query;
                getData(query, true);
                if (progressDialog != null && !progressDialog.isShowing()) {
                   //progressDialog.show();
                }
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);

        return super.onCreateOptionsMenu(menu);
    }


    public void getData(final String query, boolean newQuery) {
        if (newQuery) {
            movies.clear();
            gridFragment.gridRecyclerAdapter.notifyDataSetChanged();
            loadedPage = 0;
            reachEnd = false;
            ApiCall.Factory.getInstance().search(query, "movie", 1).enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    searchResult = response.body();
                    if (searchResult.getResponse().equals("True")) {
                        //Movie Found
                        loadedPage = 1;
                        getMovies();
                        gridFragment.message.setVisibility(View.GONE);
                        gridFragment.movieGridRecycler.setVisibility(View.VISIBLE);
                    } else {
                        //Movie not found
                        progressDialog.dismiss();
                        gridFragment.message.setText("No movies found. Try again.");
                        gridFragment.message.setVisibility(View.VISIBLE);
                        gridFragment.movieGridRecycler.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                    Log.e("MainActivity", "Failure : " + t.getMessage());
                    progressDialog.dismiss();
                    gridFragment.message.setText("Query request failed. Try again");
                    gridFragment.message.setVisibility(View.VISIBLE);
                    gridFragment.movieGridRecycler.setVisibility(View.GONE);
                }
            });
        } else {
            Log.i("newQuery",newQuery+"");
            if (!reachEnd) {
                loadedPage++;
                Log.i("PageLoaded",loadedPage+"");
                ApiCall.Factory.getInstance().search(query, "movie", loadedPage).enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                        searchResult = response.body();
                        if (searchResult.getResponse().equals("True")) {
                            //Movie Found
                            getMovies();
                        } else {
                            //Reached End
                            movies.remove(movies.size() - 1);
                            reachEnd = true;
                            Log.i("reachedEnd",reachEnd+"");
                            gridFragment.gridRecyclerAdapter.notifyItemRemoved(movies.size());
                            gridFragment.gridRecyclerAdapter.notifyDataSetChanged();
                            gridFragment.setLoaded();
                        }
                    }

                    @Override
                    public void onFailure(Call<SearchResult> call, Throwable t) {
                        movies.remove(movies.size() - 1);
                        gridFragment.gridRecyclerAdapter.notifyItemRemoved(movies.size());
                        gridFragment.gridRecyclerAdapter.notifyDataSetChanged();
                        gridFragment.setLoaded();
                    }
                });
            }
        }
    }

    public void getMovies() {
        final int[] count = {0};
        for (int i = 0; i < searchResult.getSearch().size(); i++) {
            String imdbId = searchResult.getSearch().get(i).getImdbID();
            ApiCall.Factory.getInstance().getMovie(imdbId).enqueue(new Callback<Movie>() {
                @Override
                public void onResponse(Call<Movie> call, Response<Movie> response) {
                    movies.add(response.body());
                    Log.d(TAG, movies.get(movies.size() - 1).getTitle());
                    count[0]++;
                    isDataFetchComplete(count[0]);
                }

                @Override
                public void onFailure(Call<Movie> call, Throwable t) {
                    Log.e(TAG, "Failure : " + t.getMessage());
                    count[0]++;
                    isDataFetchComplete(count[0]);
                }
            });
        }
    }

    public void isDataFetchComplete(int count){
        if (searchResult.getResponse().equals("True") && count == searchResult.getSearch().size()) {
            progressDialog.dismiss();
            for (int i = 0; i < movies.size(); i++) {
                if (movies.get(i) == null) {
                    movies.remove(i);
                    gridFragment.gridRecyclerAdapter.notifyItemRemoved(i);
                }
            }
            gridFragment.gridRecyclerAdapter.notifyDataSetChanged();
            gridFragment.setLoaded();
        }
    }

    public void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(gridFragment, "Movies");
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        public List<Fragment> fragments = new ArrayList<>();
        public List<String> fragmentTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            fragmentTitles.add(title);
        }
    }

}
