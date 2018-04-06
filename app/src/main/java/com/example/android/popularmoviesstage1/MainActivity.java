package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.utilities.MoviesDbJsonUtils;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movies>>,
        MoviesAdapter.MoviesAdapterOnClickHandler{

    private RecyclerView mMoviesRv;
    private ProgressBar mMoviesPb;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mLayoutManager;

    private static final int MOVIE_DB_LOADER = 1;

    private boolean button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesPb = (ProgressBar)findViewById(R.id.pb_loading_indicator);
        mLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        mMoviesAdapter = new MoviesAdapter(this, this);

        mMoviesRv = (RecyclerView)findViewById(R.id.recyclerview_movies);
        mMoviesRv.setHasFixedSize(true);
        mMoviesRv.setLayoutManager(mLayoutManager);
        mMoviesRv.setAdapter(mMoviesAdapter);

        button = true;
        setTitle(R.string.popular_movies);
        getSupportLoaderManager().initLoader(MOVIE_DB_LOADER, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Movies>> onCreateLoader(int id, @Nullable Bundle args) {

        return new AsyncTaskLoader<List<Movies>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                mMoviesPb.setVisibility(View.VISIBLE);
                forceLoad();
            }


            @Nullable
            @Override
            public List<Movies> loadInBackground() {

                URL moviesRequestUrl;
                if(button)
                {
                    moviesRequestUrl = NetworkUtils.buildPopularUrl();
                }
                else
                {
                    moviesRequestUrl = NetworkUtils.buildTopRatedUrl();
                }

                try
                {
                    String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);

                    return MoviesDbJsonUtils.getJson(MainActivity.this, jsonMoviesResponse);

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
    public void onLoadFinished(@NonNull Loader<List<Movies>> loader, List<Movies> data) {

        mMoviesPb.setVisibility(View.INVISIBLE);
        mMoviesAdapter.setMovieData(data);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movies>> loader) {
        mMoviesAdapter.setMovieData(null);
    }

    private void loadMoviesData()
    {
        getSupportLoaderManager().restartLoader(MOVIE_DB_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            button = true;
            mMoviesAdapter.setMovieData(null);
            loadMoviesData();
            setTitle(getString(R.string.popular_movies));
            return true;
        }

        else if(id == R.id.action_top_rated)
        {
            button = false;
            mMoviesAdapter.setMovieData(null);
            loadMoviesData();
            setTitle(getString(R.string.top_rated_movies));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String title, String plot, Double rating, String release, String poster) {

        Class destinationClass = DetailsActivity.class;
        Intent intent = new Intent(this,destinationClass);
        intent.putExtra("title", title);
        intent.putExtra("plot", plot);
        intent.putExtra("rating", rating);
        intent.putExtra("release", release);
        intent.putExtra("poster", poster);
        startActivity(intent);

    }
}
