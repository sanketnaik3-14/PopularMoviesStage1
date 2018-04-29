package com.example.android.popularmoviesstage1.utilities;

import android.content.Context;
import android.database.Cursor;

import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.data.MovieContract;

import java.util.ArrayList;
import java.util.List;

public class AddCursorToList {

    public static List<Movies> getMovList(Context context, Cursor cursor)
    {
        List<Movies> mov = new ArrayList<Movies>();

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int movId = 0;
            String title = "";
            String releaseDate = "";
            String imageUrl = "";
            Double rating = 0.0;
            String plot = "";

            movId = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
            title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            plot = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
            rating = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
            releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE));
            imageUrl = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_IMAGE));

            mov.add(new Movies(movId,title,plot,rating,releaseDate,imageUrl));
            cursor.moveToNext();
        }

        return mov;
    }
}
