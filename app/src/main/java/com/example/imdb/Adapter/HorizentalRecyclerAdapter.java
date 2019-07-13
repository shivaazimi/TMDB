package com.example.imdb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imdb.Model.Movie;
import com.example.imdb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HorizentalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        public int viewTypeItem=0;
        public int viewTypeLoading=1;
        public Context context;
        public ArrayList list;

        public HorizentalRecyclerAdapter(Context context , ArrayList list){
            this.context=context;
            this.list=list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
            if(viewType==viewTypeItem){
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_recycle_item,parent,false);
                return new com.example.imdb.Adapter.GridRecyclerAdapter.MovieViewHolder(view);
            }else if(viewType==viewTypeLoading) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar_item,parent,false);
                return new com.example.imdb.Adapter.GridRecyclerAdapter.LoadingViewHolder(view);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof GridRecyclerAdapter.MovieViewHolder){
                Movie movie=movies.get(position);
                GridRecyclerAdapter.MovieViewHolder movieViewHolder=(GridRecyclerAdapter.MovieViewHolder) holder;
                Picasso.with(context).load(movie.getPoster()).fit().into(movieViewHolder.poster);
                movieViewHolder.title.setText(movie.getTitle());
                movieViewHolder.description.setText(movie.getPlot());
            }else if(holder instanceof com.example.imdb.Adapter.GridRecyclerAdapter.LoadingViewHolder){
                com.example.imdb.Adapter.GridRecyclerAdapter.LoadingViewHolder loadingViewHolder=(com.example.imdb.Adapter.GridRecyclerAdapter.LoadingViewHolder) holder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }

        @Override
        public int getItemCount() {
            return movies==null ? 0 : movies.size();
        }

        @Override
        public int getItemViewType(int position) {
            return movies.get(position)==null ? viewTypeLoading :viewTypeItem;
        }


    }





