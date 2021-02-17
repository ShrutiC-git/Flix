package com.chaturvedi.shruti.flicksapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    String posterPath, title, overview, backdrop;
    double popularity;

    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        backdrop = jsonObject.getString("backdrop_path");
        popularity = jsonObject.getDouble("popularity");
    }

    public static List<Movie> parseArray(JSONArray jsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            movies.add(new Movie(jsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdrop);
    }

    public double getPopularity() {
        return popularity;
    }
}
