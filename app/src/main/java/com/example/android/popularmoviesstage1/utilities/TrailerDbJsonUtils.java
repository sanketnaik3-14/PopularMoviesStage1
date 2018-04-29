package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;

import com.example.android.popularmoviesstage1.pojo.Trailers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrailerDbJsonUtils {

    public static List<Trailers> getJson(Context context, String trailerJsonStr) throws JSONException
    {
        List<Trailers> trailer = new ArrayList<>();
        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        JSONArray results = trailerJson.getJSONArray("results");

        for(int i = 0; i < results.length(); i++)
        {
            String trailerKey = "";
            String trailerDescription = "";

            JSONObject object = results.getJSONObject(i);
            trailerKey = object.getString("key");
            trailerDescription = object.getString("name");

            trailer.add(new Trailers(trailerKey, trailerDescription));
        }
        return trailer;
    }
}
