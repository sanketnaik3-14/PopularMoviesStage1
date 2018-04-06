package com.example.android.popularmoviesstage1;

public class Movies {

    private String mTitle;
    private String mOverview;
    private double mRating;
    private String mReleaseDate;
    private String mPosterId;

    public Movies(String title, String overview, double rating, String releaseDate, String posterId)
    {
        mTitle = title;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
        mPosterId = posterId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public double getmRating() {
        return mRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmPosterId() {
        return mPosterId;
    }
}
