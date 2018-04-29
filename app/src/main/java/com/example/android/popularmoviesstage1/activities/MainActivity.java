package com.example.android.popularmoviesstage1.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.http.HttpResponseCache;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.fragments.MovieDetailFragment;
import com.example.android.popularmoviesstage1.fragments.MovieListFragment;
import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.adapters.MoviesAdapter;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.data.MovieContract;
import com.example.android.popularmoviesstage1.utilities.AddCursorToList;
import com.example.android.popularmoviesstage1.utilities.MoviesDbJsonUtils;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListFragment.OnMovieClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            File httpCacheDir = new File(getApplicationContext().getCacheDir(),"https");
            long httpCacheSize = 20 * 1024 * 1024;

            HttpResponseCache.install(httpCacheDir,httpCacheSize);
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }

        if(getResources().getBoolean(R.bool.isTwoPane))
        {
            MovieListFragment movieListFragment = new MovieListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.list_container, movieListFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else
        {
            MovieListFragment movieListFragment = new MovieListFragment();
            MovieDetailFragment movieDetailFragment = (MovieDetailFragment)
                    getSupportFragmentManager().findFragmentById(R.id.details_container);

            FragmentManager fragmentManager = getSupportFragmentManager();

            if(movieDetailFragment != null)
            {
                fragmentManager.beginTransaction()
                        .remove(movieDetailFragment)
                        .commit();
            }

                    fragmentManager.beginTransaction()
                    .add(R.id.list_container, movieListFragment)
                    .addToBackStack(null)
                    .commit();
        }

    }

    @Override
    public void onMovieSelected(Movies movie) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("movieFromMainActivity", movie);

        movieDetailFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.details_container,movieDetailFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


}
