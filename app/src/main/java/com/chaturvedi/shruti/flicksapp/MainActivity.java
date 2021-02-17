package com.chaturvedi.shruti.flicksapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.chaturvedi.shruti.flicksapp.adapters.MovieAdapter;
import com.chaturvedi.shruti.flicksapp.models.Movie;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    static final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US&page=1";
    static final String TAG = "MainActivity";
    RecyclerView rvMovies;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movies = new ArrayList<>();

        rvMovies = findViewById(R.id.rvMovies);

        final MovieAdapter movieAdapter = new MovieAdapter(this, movies);
        rvMovies.setAdapter(movieAdapter);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + jsonArray.toString());
                    movies.addAll(Movie.parseArray(jsonArray));
                    movieAdapter.notifyDataSetChanged();
                    Log.i(TAG, "Moves from array: " + movies.toString());
                } catch (JSONException e) {
                    Log.e(TAG, "JSON Exception: " + e);
                    e.printStackTrace();
                }
                Log.d(TAG, "OnSuccess Listener");
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e(TAG, "Error: ", throwable);
            }
        });
    }
} 