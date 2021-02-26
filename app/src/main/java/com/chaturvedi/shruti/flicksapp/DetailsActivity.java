package com.chaturvedi.shruti.flicksapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chaturvedi.shruti.flicksapp.models.Movie;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailsActivity extends YouTubeBaseActivity {

    TextView tvTitle, tvOverview, tvDate;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    private final String apiKey = "AIzaSyAJAReWxV_O14o8TeAye090Mz3BIhL-96I";

    public static final String VIDEO_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed&language=en-US";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        tvDate = findViewById(R.id.tvDate);
        ratingBar = findViewById(R.id.ratingBar);
        youTubePlayerView = findViewById(R.id.player);

        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());
        tvDate.setText(movie.getDate());

        final float rating = (float) movie.getRating();

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEO_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if (results.length() == 0) {
                        return;
                    }
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    initializeYoutube(youtubeKey, rating);

                } catch (JSONException e) {
                    Log.e("Detail_activity", e + "");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });

    }

    private void initializeYoutube(final String youtubeKey, final float rating) {

        youTubePlayerView.initialize(apiKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("Details Activity", "onSuccess");
                if (rating > 6) {
                    //youTubePlayer.cueVideo(youtubeKey);
                    youTubePlayer.loadVideo(youtubeKey);
                } else {
                    youTubePlayer.cueVideo(youtubeKey);
                }

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("Details Activity", "Failed to Initialize" + youTubeInitializationResult);
            }
        });
    }
}