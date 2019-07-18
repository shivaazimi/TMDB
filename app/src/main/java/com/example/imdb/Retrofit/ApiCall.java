package com.example.imdb.Retrofit;

import com.example.imdb.Model.CelebrityResult;
import com.example.imdb.Model.MovieResult;
import com.example.imdb.Model.OMDBMovie;
import com.example.imdb.Model.SearchResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {

    //OMDB
    String OMDB_BASE_URL = "http://www.omdbapi.com";
    String OMDB_API_KEY ="?apikey="+"36b6dc1f"+"&";

    @GET("/"+ OMDB_API_KEY)
    Call<SearchResult> search(@Query("s") String query, @Query("type") String type, @Query("page") int page);

    @GET("/"+ OMDB_API_KEY)
    Call<OMDBMovie> getMovie(@Query("i") String imdbId);


    //TMDB
    String TMDB_BASE_URL="https://api.themoviedb.org/3/";
    String TMDB_API_KEY="?api_key="+"424071afff63d5e333e67ffc70d38502"+"&";

    @GET("person/popular/"+ TMDB_API_KEY)
    Call<CelebrityResult> popularPerson(@Query("page") int page);

    @GET("movie/popular"+TMDB_API_KEY)
    Call<MovieResult> poularMovie(@Query("page") int page);

    @GET("tv/popular"+TMDB_API_KEY)
    Call<TVShowResult> poularTVShow(@Query("page") int page);


    class Factory {
        public static ApiCall omdb;
        public static ApiCall tmdb;

        public static ApiCall OMDB() {
            if (omdb == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create()).baseUrl(OMDB_BASE_URL).build();
                omdb = retrofit.create(ApiCall.class);
                return omdb;
            } else {
                return omdb;
            }
        }

        public static ApiCall TMDB() {
            if (tmdb == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create()).baseUrl(TMDB_BASE_URL).build();
                tmdb = retrofit.create(ApiCall.class);
                return tmdb;
            } else {
                return tmdb;
            }
        }
    }
}

