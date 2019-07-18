package com.example.imdb.Model;

public class Model {
    public static final int LOADING_TYPE=0;
    public static final int MOVIE_TYPE=1;
    public static final int CELEBRITY_TYPE=2;
    public static final int SERIES_TYPE=3;
    public static final int TMDB_MOVIE=4;

    public int TYPE;

    public Model(int TYPE){
        this.TYPE=TYPE;
    }
}
