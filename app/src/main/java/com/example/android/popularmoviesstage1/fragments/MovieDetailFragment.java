package com.example.android.popularmoviesstage1.fragments;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.activities.DetailsActivity;
import com.example.android.popularmoviesstage1.activities.ReviewsActivity;
import com.example.android.popularmoviesstage1.adapters.TrailersAdapter;
import com.example.android.popularmoviesstage1.data.MovieContract;
import com.example.android.popularmoviesstage1.databinding.FragmentMovieDetailBinding;
import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.pojo.Trailers;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;
import com.example.android.popularmoviesstage1.utilities.RecyclerViewMarginDecoration;
import com.example.android.popularmoviesstage1.utilities.TrailerDbJsonUtils;

import java.net.URL;
import java.util.List;

public class MovieDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Trailers>>,
        TrailersAdapter.TrailersAdapterOnClickHandler {


    ToggleButton toggleButton;
    TextView mReviews;
    Movies movie;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    boolean check;
    String name;

    private RecyclerView mTrailersRv;
    private TrailersAdapter trailersAdapter;
    private LinearLayoutManager layoutManager;

    private static final int TRAILER_DB_LOADER = 100;

    FragmentMovieDetailBinding mBinding;
    private static final String SCHEME = "https";
    private static final String AUTHORITY = "image.tmdb.org";
    private static final String PATHT = "t";
    private static final String PATHP = "p";
    private static final String SIZE = "w780";

    public static final String TAG = MovieDetailFragment.class.getSimpleName();

    public MovieDetailFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_movie_detail, container, false);

        View rootView = mBinding.getRoot();

        if(savedInstanceState != null)
        {
            movie = savedInstanceState.getParcelable("myObject");
        }
        else
        {

            if (getActivity().getResources().getBoolean(R.bool.isTwoPane))
            {
                movie = getArguments().getParcelable(("movieFromMainActivity"));
            }
            else
            {
                movie = getArguments().getParcelable("movieFromDetailsActivity");
            }

        }

        toggleButton = (ToggleButton)rootView.findViewById(R.id.toggleButtonFavourite);
        mReviews = (TextView)rootView.findViewById(R.id.textViewReviews);

        trailersAdapter = new TrailersAdapter(getActivity(), this);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);

        mTrailersRv = (RecyclerView)rootView.findViewById(R.id.recyclerViewTrailers);
        mTrailersRv.setHasFixedSize(true);
        mTrailersRv.setLayoutManager(layoutManager);
        mTrailersRv.setAdapter(trailersAdapter);
        mTrailersRv.addItemDecoration(new RecyclerViewMarginDecoration(getActivity()));


        name = String.valueOf(movie.getmMovieId());
        prefs = getActivity().getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = prefs.edit();
        check = prefs.getBoolean(name, false);

        if(check)
        {
            toggleButton.setChecked(true);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_favorite_yellow_56dp));
        }
        else
        {
            toggleButton.setChecked(false);
            toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_favorite_grey_56dp));
        }

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_favorite_yellow_56dp));
                    ContentValues cv = new ContentValues();

                    cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, movie.getmMovieId());
                    cv.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getmTitle());
                    cv.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getmOverview());
                    cv.put(MovieContract.MovieEntry.COLUMN_RATING, movie.getmRating());
                    cv.put(MovieContract.MovieEntry.COLUMN_RELEASE, movie.getmReleaseDate());
                    cv.put(MovieContract.MovieEntry.COLUMN_IMAGE, movie.getmPosterId());

                    Uri uriForFav = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);
                    Toast.makeText(getActivity(),uriForFav.toString(),Toast.LENGTH_SHORT).show();

                    editor.putBoolean(name,true);
                    editor.apply();

                }
                else
                {
                    toggleButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.ic_favorite_grey_56dp));
                    editor.putBoolean(name,false);
                    editor.apply();

                    String selection = MovieContract.MovieEntry.COLUMN_MOVIE_ID + "=?";
                    String args = String.valueOf(movie.getmMovieId());
                    String[] selectionArgs = new String[]{args};

                    int noRowsDeleted = getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI, selection, selectionArgs);
                    Toast.makeText(getActivity(),noRowsDeleted + " of rows deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReviewsActivity.class);
                intent.putExtra("movieId", movie.getmMovieId());
                intent.putExtra("movieName", movie.getmTitle());
                startActivity(intent);
            }
        });

        bindDetails(movie);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Log.i(TAG,"onCreate");
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("myObject",movie);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id)
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindDetails(Movies movie)
    {
        String str = Double.toString(movie.getmRating()) + " Stars";
        String imageString = movie.getmPosterId();
        imageString = imageString.replace("/","");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(PATHT)
                .appendPath(PATHP)
                .appendPath(SIZE)
                .appendPath(imageString);

        Glide.with(this)
                .load(builder.build().toString())
                .into(mBinding.imageViewMoviePoster);
        getActivity().setTitle(movie.getmTitle());
        mBinding.textViewTitleContent.setText(movie.getmTitle());
        mBinding.textViewReleaseContent.setText(movie.getmReleaseDate());
        mBinding.textViewRatingContent.setText(str);
        mBinding.textViewOverviewContent.setText(movie.getmOverview());
        mBinding.textViewReviews.setText(getResources().getString(R.string.reviews));
        mBinding.textViewTrailers.setText(getResources().getString(R.string.trailers));

        Bundle bundle = new Bundle();
        bundle.putInt("movieId", movie.getmMovieId());
        getActivity().getSupportLoaderManager().restartLoader(TRAILER_DB_LOADER, bundle, this);
    }

    @NonNull
    @Override
    public Loader<List<Trailers>> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<List<Trailers>>(getActivity()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Nullable
            @Override
            public List<Trailers> loadInBackground() {

                URL trailerRequestUrl;
                int movieId;
                movieId = args.getInt("movieId");
                trailerRequestUrl = NetworkUtils.buildTrailersUrl(movieId);

                try
                {
                    String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(trailerRequestUrl);
                    return TrailerDbJsonUtils.getJson(getActivity(), jsonTrailersResponse);
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
    public void onLoadFinished(@NonNull Loader<List<Trailers>> loader, List<Trailers> data) {
        trailersAdapter.setTrailerData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Trailers>> loader) {

    }



    @Override
    public void onClick(Trailers trailer) {

        String key = trailer.getmTrailerKey();

        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/watch?v=" + key));

        try
        {
            startActivity(appIntent);
        }
        catch(ActivityNotFoundException ex)
        {
            startActivity(webIntent);
        }

    }
}
