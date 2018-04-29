package com.example.android.popularmoviesstage1.utilities;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String KEY_PARAM = "api_key";
    private static final String LANGUAGE_PARAM = "language";
    private static final String PAGE_PARAM = "page";
    private static final String SORT_PARAM = "sort_by";

    private static final String apiKey = "599a38e27a98c3bea1547cee07db81df";
    private static final String language = "en-US";
    private static final String page = "1";
    private static final String voteCount = "vote_count.desc";
    private static final String popularity = "popularity.desc";

    public static URL buildPopularUrl()
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter(KEY_PARAM, apiKey)
                .appendQueryParameter(SORT_PARAM, popularity)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page);

        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        }
        catch(MalformedURLException mfe)
        {
            mfe.printStackTrace();
        }

        return url;
    }

    public static URL buildTopRatedUrl()
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("discover")
                .appendPath("movie")
                .appendQueryParameter(KEY_PARAM, apiKey)
                .appendQueryParameter(SORT_PARAM, voteCount)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(PAGE_PARAM, page);

        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        }
        catch(MalformedURLException mfe)
        {
            mfe.printStackTrace();
        }

        return url;
    }

    public static URL buildReviewsUrl(int id)
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(id))
                .appendPath("reviews")
                .appendQueryParameter(KEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, language);

        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        }
        catch(MalformedURLException mfe)
        {
            mfe.printStackTrace();
        }
        return url;
    }

    public static URL buildTrailersUrl(int id)
    {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(String.valueOf(id))
                .appendPath("videos")
                .appendQueryParameter(KEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, language);

        URL url = null;
        try
        {
            url = new URL(builder.build().toString());
        }
        catch(MalformedURLException mfe)
        {
            mfe.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setUseCaches(true);
        int maxStale = 60 * 60 * 24 * 28;
        urlConnection.addRequestProperty("Cache-Control", "max-stale=" + maxStale);
        try
        {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if(hasInput)
            {
                return scanner.next();
            }
            else
            {
                return null;
            }
        }
        finally
        {
            urlConnection.disconnect();
        }
    }
}
