package com.example.android.popularmoviesstage1.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.adapters.ReviewsAdapter;
import com.example.android.popularmoviesstage1.pojo.Reviews;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.example.android.popularmoviesstage1.utilities.ReviewDbJsonUtils;

import java.net.URL;
import java.util.List;

public class ReviewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Reviews>> {

    private int movieId;
    private static final String SCHEME = "https";
    private static final String AUTHORITY = "api.themoviedb.org";
    private static final String PATH_THREE = "3";
    private static final String PATH_MOVIE = "movie";
    private static final String PATH_REVIEWS = "reviews";

    private static final String KEY_PARAM = "api_key";
    private static final String KEY_LANGUAGE = "language";
    private static final String API_KEY = "599a38e27a98c3bea1547cee07db81df";
    private static final String LANGUAGE = "en-US";

    private static final int REVIEW_DB_LOADER = 24;

    private ProgressBar pb_review;
    private RecyclerView reviewRecyclerView;
    private ReviewsAdapter reviewsAdapter;
    private LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        pb_review = (ProgressBar)findViewById(R.id.pb_loading_indicator_review);
        reviewsAdapter = new ReviewsAdapter(this);
        layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);

        reviewRecyclerView = (RecyclerView)findViewById(R.id.recyclerview_reviews);
        reviewRecyclerView.setHasFixedSize(true);
        reviewRecyclerView.setLayoutManager(layoutManager);
        reviewRecyclerView.setAdapter(reviewsAdapter);

        Intent receivingIntent = getIntent();
        String movieName = receivingIntent.getStringExtra("movieName");
        movieName = movieName + " - Reviews";
        movieId = receivingIntent.getIntExtra("movieId", 0);
        setTitle(movieName);

        Bundle bundle = new Bundle();
        bundle.putInt("movieId", movieId);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getSupportLoaderManager().restartLoader(REVIEW_DB_LOADER, bundle, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id)
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<List<Reviews>> onCreateLoader(int id, @Nullable final Bundle args) {

        return new AsyncTaskLoader<List<Reviews>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                pb_review.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Nullable
            @Override
            public List<Reviews> loadInBackground() {

                URL reviewRequestUrl;
                int movieId;
                movieId = args.getInt("movieId");
                reviewRequestUrl = NetworkUtils.buildReviewsUrl(movieId);

                try
                {
                    String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewRequestUrl);
                    return ReviewDbJsonUtils.getJson(ReviewsActivity.this, jsonReviewsResponse);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    return null;
                }

            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Reviews>> loader, List<Reviews> data) {

        pb_review.setVisibility(View.INVISIBLE);
        reviewsAdapter.setReviewData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Reviews>> loader) {

    }
}
