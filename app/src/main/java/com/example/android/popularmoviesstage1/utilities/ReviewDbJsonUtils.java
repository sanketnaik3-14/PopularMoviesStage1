package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;

import com.example.android.popularmoviesstage1.pojo.Reviews;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewDbJsonUtils {

    public static List<Reviews> getJson(Context context, String reviewJsonStr) throws JSONException
    {
        List<Reviews> review = new ArrayList<>();
        JSONObject reviewJson = new JSONObject(reviewJsonStr);

        JSONArray results = reviewJson.getJSONArray("results");

        for(int i = 0; i < results.length(); i++)
        {
            String author = "";
            String movieReview = "";

            JSONObject object = results.getJSONObject(i);
            author = object.getString("author");
            movieReview = object.getString("content");

            review.add(new Reviews(author, movieReview));
        }

        return review;
    }
}
