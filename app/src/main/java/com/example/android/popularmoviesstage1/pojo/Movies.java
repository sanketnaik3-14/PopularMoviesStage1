package com.example.android.popularmoviesstage1.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Movies implements Parcelable {

    private int mMovieId;
    private String mTitle;
    private String mOverview;
    private double mRating;
    private String mReleaseDate;
    private String mPosterId;

    public Movies(int movieId,String title, String overview, double rating, String releaseDate, String posterId)
    {
        mMovieId = movieId;
        mTitle = title;
        mOverview = overview;
        mRating = rating;
        mReleaseDate = releaseDate;
        mPosterId = posterId;
    }

    public int getmMovieId() { return mMovieId; }

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

    public Movies(Parcel in) {
        mMovieId = in.readInt();
        mTitle = in.readString();
        mOverview = in.readString();
        mRating = in.readDouble();
        mReleaseDate = in.readString();
        mPosterId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(mMovieId);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeDouble(mRating);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

}
