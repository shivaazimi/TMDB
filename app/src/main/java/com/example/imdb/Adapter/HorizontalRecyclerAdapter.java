package com.example.imdb.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.imdb.Model.Celebrity;
import com.example.imdb.Model.Model;
import com.example.imdb.Model.Movie;
import com.example.imdb.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HorizontalRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int LoadingType = 0;
    public static final int MovieType = 1;
    public static final int CelebrityType = 2;
    public static final int SeriesType = 3;

    public Context context;
    public ArrayList<Model> list;

    public HorizontalRecyclerAdapter(Context context, ArrayList<Model> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LoadingType:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_bar_item, parent, false);
                return new LoadingViewHolder(view);
            case MovieType:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_recycler_item, parent, false);
                return new MovieViewHolder(view);
            case CelebrityType:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.celebrity_recycler_item, parent, false);
                return new CelebrityViewHolder(view);

        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder)
        {
            setUpMovieViewHolder(holder, position);
        } else if (holder instanceof LoadingViewHolder)
        {
            setUpLoadingViewHolder(holder);
        } else if (holder instanceof CelebrityViewHolder)
        {
            setUpCelebrityViewHolder(holder, position);
        }
    }

    public void setUpMovieViewHolder(RecyclerView.ViewHolder holder, int position) {
        Movie movie = (Movie) list.get(position);
        MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
        Picasso.with(context).load(movie.getPoster()).fit().into(movieViewHolder.poster);
        movieViewHolder.title.setText(movie.getTitle());
        movieViewHolder.description.setText(movie.getPlot());
    }

    public void setUpLoadingViewHolder(RecyclerView.ViewHolder holder) {
        LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
        loadingViewHolder.progressBar.setIndeterminate(true);
    }

    public void setUpCelebrityViewHolder(RecyclerView.ViewHolder holder, int position) {
        Celebrity celebrity = (Celebrity) list.get(position);
        CelebrityViewHolder celebrityViewHolder = (CelebrityViewHolder) holder;
        String profilePath = "https://image.tmdb.org/t/p/original" + celebrity.getProfilePath();
        Picasso.with(context).load(profilePath).fit().into(celebrityViewHolder.portrait);
        celebrityViewHolder.name.setText(celebrity.getName());
        celebrityViewHolder.score.setText("Popularity : " + celebrity.getPopularity() + "");
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null)
            return LoadingType;
        else
            return list.get(position).TYPE;
    }

    static class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
        }
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

    static class CelebrityViewHolder extends RecyclerView.ViewHolder {

        ImageView portrait;
        TextView name, score;

        public CelebrityViewHolder(View itemView) {
            super(itemView);
            portrait = (ImageView) itemView.findViewById(R.id.poster);
            name = (TextView) itemView.findViewById(R.id.title);
            score = (TextView) itemView.findViewById(R.id.description);
        }
    }

}



