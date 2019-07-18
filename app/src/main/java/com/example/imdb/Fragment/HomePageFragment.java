package com.example.imdb.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.imdb.Adapter.VerticalRecyclerAdapter;
import com.example.imdb.Interface.OnLoadMoreListener;
import com.example.imdb.Model.Celebrity;
import com.example.imdb.Model.CelebrityResult;
import com.example.imdb.Model.Model;
import com.example.imdb.Model.MovieResult;
import com.example.imdb.Model.ParentModel;
import com.example.imdb.Model.TMDBMovie;
import com.example.imdb.Model.TVShowResult;
import com.example.imdb.R;
import com.example.imdb.Retrofit.ApiCall;
import com.example.imdb.Utility.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePageFragment extends Fragment {

    public RecyclerView recyclerView;
    public GridLayoutManager gridLayoutManager;
    public VerticalRecyclerAdapter recyclerAdapter;
    private ArrayList<ParentModel> models;
    MovieResult movieResult;
    CelebrityResult celebrutyResult;
    private OnLoadMoreListener mOnLoadMoreListener;


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
        initialize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_reycler, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.vertical_recycler);

        if (models != null) {
            //Grid Manager
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(gridLayoutManager);


            //RecycleView Adapter
            recyclerAdapter = new VerticalRecyclerAdapter(getContext(), models);
            recyclerView.setAdapter(recyclerAdapter);

            //item Animator
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            //item Decoration
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(7), false));

        }
        recyclerView.setVisibility(View.GONE);
    }

    public void initialize() {
        models = new ArrayList<>();
        ParentModel.Type[] types = ParentModel.Type.values();
        for (ParentModel.Type type : types) {
            ParentModel model = new ParentModel();
            switch (type) {
                case Movie:
                    model.setTitle(ParentModel.Type.Movie + "");
                    model.setModels(getMovies());
                    break;
                case Celebrity:
                    model.setTitle(ParentModel.Type.Celebrity + "");
                    model.setModels(getCelebrities());
                    break;
                case TVShow:
                    model.setTitle(ParentModel.Type.Celebrity + "");
                    model.setModels(getCelebrities());

            }
        }
        models.add(new ParentModel());
    }


    public List getMovies() {
        loadedPage = 1;
        ApiCall.Factory.TMDB().poularMovie(1).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                movieResult = response.body();
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
        if (movieResult != null)
            return movieResult.getResults();
        else
            return null;
    }

    public List getCelebrities() {
        loadedPage = 1;
        ApiCall.Factory.TMDB().popularPerson(loadedPage).enqueue(new Callback<CelebrityResult>() {

            @Override
            public void onResponse(Call<CelebrityResult> call, Response<CelebrityResult> response) {
                celebrutyResult = response.body();
            }

            @Override
            public void onFailure(Call<CelebrityResult> call, Throwable t) {
            }
        });
        if (movieResult != null)
            return movieResult.getResults();
        else
            return null;
    }

    public List getTVShow() {
        loadedPage = 1;
        ApiCall.Factory.TMDB().poularTVShow(loadedPage).enqueue(new Callback<TVShowResult>() {
            @Override
            public void onResponse(Call<TVShowResult> call, Response<TVShowResult> response) {

            }

            @Override
            public void onFailure(Call<TVShowResult> call, Throwable t) {

            }
        }
        if (movieResult != null)
            return movieResult.getResults();
        else
            return null;
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
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics()));
    }

}
