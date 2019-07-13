package com.example.imdb.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imdb.Model.Model;
import com.example.imdb.Model.Movie;
import com.example.imdb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GridRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public int viewTypeItem = 0;
    public int viewTypeLoading = 1;
    public Context context;
    public ArrayList<Model> list;

    public GridRecyclerAdapter(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == viewTypeItem) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_recycle_item, parent, false);
            return new MovieViewHolder(view);
        } else if (viewType == viewTypeLoading) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            Movie movie = (Movie) list.get(position);
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            Picasso.with(context).load(movie.getPoster()).fit().into(movieViewHolder.poster);
            movieViewHolder.title.setText(movie.getTitle());
            movieViewHolder.description.setText(movie.getPlot());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? viewTypeLoading : viewTypeItem;
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;
        TextView title, description;

        public MovieViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.poster);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
        }
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
        }
    }
}



