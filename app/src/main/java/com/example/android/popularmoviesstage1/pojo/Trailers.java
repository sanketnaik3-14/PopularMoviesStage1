package com.example.android.popularmoviesstage1.pojo;

public class Trailers {

    private String mTrailerKey;
    private String mTrailerDescription;

    public Trailers(String trailerKey, String trailerDescription)
    {
        mTrailerKey = trailerKey;
        mTrailerDescription = trailerDescription;
    }

    public String getmTrailerKey() {
        return mTrailerKey;
    }

    public String getmTrailerDescription() {
        return mTrailerDescription;
    }
}
