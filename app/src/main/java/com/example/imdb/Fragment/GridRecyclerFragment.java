package com.example.imdb.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.imdb.Adapter.RecyclerAdapter;
import com.example.imdb.Activity.MainActivity;
import com.example.imdb.Model.Model;
import com.example.imdb.R;
import com.example.imdb.Utility.GridSpacingItemDecoration;
import com.example.imdb.Interface.OnLoadMoreListener;

import java.util.ArrayList;

public class GridRecyclerFragment extends Fragment {


    public RecyclerView movieGridRecycler;
    public GridLayoutManager gridLayoutManager;
    public RecyclerAdapter recyclerAdapter;
    public TextView message;
    private ArrayList<Model> models;
    private OnLoadMoreListener mOnLoadMoreListener;
    public boolean loading;
    public int totalItemCount;
    public int lastVisibleItem;
    public int visibleThreshold=4;

    public GridRecyclerFragment() {
        // Required empty public constructor
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public static GridRecyclerFragment newInstance() {
        GridRecyclerFragment fragment = new GridRecyclerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.grid_recycler, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        models = MainActivity.movies;
    }

    @Override
    public void onViewCreated( View view,  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieGridRecycler=view.findViewById(R.id.grid_recycler);
        message=view.findViewById(R.id.message);

        if(models !=null) {
            //Grid Manager
            gridLayoutManager = new GridLayoutManager(getContext(), 2);
            movieGridRecycler.setLayoutManager(gridLayoutManager);

            //RecycleView Adapter
            recyclerAdapter = new RecyclerAdapter(getContext(), models);
            movieGridRecycler.setAdapter(recyclerAdapter);

            //item Animator
            movieGridRecycler.setItemAnimator(new DefaultItemAnimator());

            //item Decoration
            movieGridRecycler.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(7), false));

            movieGridRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount=gridLayoutManager.getItemCount();
                    lastVisibleItem=gridLayoutManager.findLastVisibleItemPosition();

                    Log.i("onScroll","totalItemCount :"+totalItemCount+" lastVisibleItem : "+lastVisibleItem);
                    if(!loading && totalItemCount<=(lastVisibleItem+visibleThreshold)){
                        Log.i("exceed threashold", "mustBeLoad");
                        if(mOnLoadMoreListener!=null){
                            mOnLoadMoreListener.onLoadMore();
                        }
                        loading=true;
                    }
                }

            });
        }
        movieGridRecycler.setVisibility(View.GONE);
        message.setVisibility(View.VISIBLE);
    }

    public int dpToPx(int dp){
        DisplayMetrics metrics=getResources().getDisplayMetrics();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,
                getResources().getDisplayMetrics()));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnLoadMoreListener = null;
    }

    public void setLoaded(){
        loading=false;
    }
}
