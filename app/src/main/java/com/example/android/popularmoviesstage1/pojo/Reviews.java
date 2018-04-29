package com.example.android.popularmoviesstage1.pojo;

public class Reviews {

    private String mAuthor;
    private String mContent;

    public Reviews(String author, String content)
    {
        mAuthor = author;
        mContent = content;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmContent() {
        return mContent;
    }
}
