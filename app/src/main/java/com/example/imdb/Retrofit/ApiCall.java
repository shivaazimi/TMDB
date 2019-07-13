package com.example.imdb.Retrofit;

import com.example.imdb.Model.Movie;
import com.example.imdb.Model.SearchResult;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCall {

    String BASE_URL = "http://www.omdbapi.com";
    String API_KEY="?apikey="+"36b6dc1f"+"&";


    @GET("/"+API_KEY)
    Call<SearchResult> search(@Query("s") String query, @Query("type") String type, @Query("page") int page);

    @GET("/"+API_KEY)
    Call<Movie> getMovie(@Query("i") String imdbId);



    class Factory {
        public static ApiCall service;

        public static ApiCall getInstance() {
            if (service == null) {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build();
                service = retrofit.create(ApiCall.class);
                return service;
            } else {
                return service;
            }
        }


    }
}

