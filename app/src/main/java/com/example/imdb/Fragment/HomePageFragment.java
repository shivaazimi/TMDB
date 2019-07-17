package com.example.imdb.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imdb.Adapter.RecyclerAdapter;
import com.example.imdb.Interface.OnLoadMoreListener;
import com.example.imdb.Model.Celebrity;
import com.example.imdb.Model.CelebrutyResult;
import com.example.imdb.Model.Model;
import com.example.imdb.R;
import com.example.imdb.Retrofit.ApiCall;
import com.example.imdb.Utility.GridSpacingItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageFragment extends Fragment {

    public RecyclerView recyclerView;
    public GridLayoutManager gridLayoutManager;
    public RecyclerAdapter recyclerAdapter;
    private ArrayList<Model> models;
    private OnLoadMoreListener mOnLoadMoreListener;
    public boolean loading;
    public int totalItemCount;
    public int lastVisibleItem;
    public int visibleThreshold = 4;

    public CelebrutyResult celebrutyResult;
    public boolean reachEnd;
    public int loadedPage;

    public HomePageFragment() {
        // Required empty public constructor
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public static HomePageFragment newInstance(String param1, String param2) {
        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        models = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_reycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.horizontal_recycler);

        if (models != null) {
            //Grid Manager
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
            gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(gridLayoutManager);


            //RecycleView Adapter
            recyclerAdapter = new RecyclerAdapter(getContext(), models);
            recyclerView.setAdapter(recyclerAdapter);

            getData(true);
            setUpLazyLoad();

            //item Animator
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            //item Decoration
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(7), false));

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }

            });
        }
        recyclerView.setVisibility(View.GONE);
    }

    public void setUpLazyLoad() {
        setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!reachEnd) {
                    getData(false);
                }
            }
        });
    }

    public void getData(boolean newLoad) {
        if (newLoad) {
            models.clear();
            loadedPage = 1;
            recyclerAdapter.notifyDataSetChanged();
            ApiCall.Factory.TMDB().popularPerson(loadedPage).enqueue(new Callback<CelebrutyResult>() {

                @Override
                public void onResponse(Call<CelebrutyResult> call, Response<CelebrutyResult> response) {
                    celebrutyResult = response.body();
                    getCelebrities();
                    recyclerView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<CelebrutyResult> call, Throwable t) {
                    Log.e("HomePageFragment", "Failure : " + t.getMessage());
                    recyclerView.setVisibility(View.GONE);
                }
            });
        } else {

            if (!reachEnd) {
                loadedPage++;
                ApiCall.Factory.TMDB().popularPerson(loadedPage).enqueue(new Callback<CelebrutyResult>() {
                    @Override
                    public void onResponse(Call<CelebrutyResult> call, Response<CelebrutyResult> response) {
                        celebrutyResult = response.body();
                        if (celebrutyResult.getTotalPages() > loadedPage) {
                            reachEnd = true;
                            setLoaded();
                        } else {
                            getCelebrities();
                        }
                    }

                    @Override
                    public void onFailure(Call<CelebrutyResult> call, Throwable t) {

                    }
                });
            }
        }
    }

    public void getCelebrities() {
        for (Celebrity celebrity : celebrutyResult.getResults()) {
            models.add(celebrity);
            recyclerAdapter.notifyItemInserted(models.size() - 1);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnLoadMoreListener = null;
    }

    public int dpToPx(int dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics()));

    }


    public void setLoaded() {
        loading = false;
    }
}
