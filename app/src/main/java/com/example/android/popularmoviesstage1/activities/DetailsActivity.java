package com.example.android.popularmoviesstage1.activities;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstage1.adapters.TrailersAdapter;
import com.example.android.popularmoviesstage1.fragments.MovieDetailFragment;
import com.example.android.popularmoviesstage1.fragments.MovieListFragment;
import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.data.MovieContract;
import com.example.android.popularmoviesstage1.pojo.Trailers;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.example.android.popularmoviesstage1.utilities.RecyclerViewMarginDecoration;
import com.example.android.popularmoviesstage1.utilities.TrailerDbJsonUtils;

import java.net.URL;
import java.util.List;

public class DetailsActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);

        Intent receivingIntent = getIntent();
        if(savedInstanceState == null)
        {
            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            Movies movieReceived = receivingIntent.getParcelableExtra("movies");

            Bundle bundle = new Bundle();
            bundle.putParcelable("movieFromDetailsActivity", movieReceived);

            movieDetailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.movie_details_container, movieDetailFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else
        {
            MovieDetailFragment movieDetailFragment = (MovieDetailFragment)
                    getSupportFragmentManager().findFragmentById(R.id.movie_details_container);
        }

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