package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;

import com.example.android.popularmoviesstage1.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MoviesDbJsonUtils {

    public static List<Movies> getJson(Context context, String moviesJsonStr) throws JSONException
    {
        List<Movies> movie = new ArrayList<>();
        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        JSONArray results = moviesJson.getJSONArray("results");

        for(int i = 0; i < results.length(); i++)
        {
            String title = "";
            String releaseDate ="";
            String imageUrl = "";
            Double rating = 0.0;
            String plot = "";

            JSONObject object = results.getJSONObject(i);

            title = object.getString("title");
            rating = object.getDouble("vote_average");
            releaseDate = object.getString("release_date");
            plot = object.getString("overview");
            imageUrl = object.getString("poster_path");

            movie.add(new Movies(title,plot,rating,releaseDate,imageUrl));
        }

        return movie;
    }
}
