package com.example.android.popularmoviesstage1.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.activities.DetailsActivity;
import com.example.android.popularmoviesstage1.activities.MainActivity;
import com.example.android.popularmoviesstage1.adapters.MoviesAdapter;
import com.example.android.popularmoviesstage1.data.MovieContract;
import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.utilities.AddCursorToList;
import com.example.android.popularmoviesstage1.utilities.MoviesDbJsonUtils;
import com.example.android.popularmoviesstage1.utilities.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Movies>>,
        MoviesAdapter.MoviesAdapterOnClickHandler{

    private RecyclerView mMoviesRv;
    private ProgressBar mMoviesPb;
    private MoviesAdapter mMoviesAdapter;
    private GridLayoutManager mLayoutManager;
    private static final int MOVIE_DB_LOADER = 1;
    private boolean button;
    public static final String TAG = MovieListFragment.class.getSimpleName();
    private Cursor cursor;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    OnMovieClickListener mCallBack;

    public interface OnMovieClickListener {
        void onMovieSelected(Movies movie);
    }

    public MovieListFragment()
    {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        pref = getActivity().getSharedPreferences("MyPref" , Context.MODE_PRIVATE);
        editor = pref.edit();

        mMoviesPb = (ProgressBar)rootView.findViewById(R.id.pb_loading_indicator);

        if(getActivity().getResources().getBoolean(R.bool.isHandsetPortrait))
        {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        }
        else if (getActivity().getResources().getBoolean(R.bool.isHandsetLand))
        {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        else if (getActivity().getResources().getBoolean(R.bool.is600TabletPortrait))
        {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        else if (getActivity().getResources().getBoolean(R.bool.is600TabletLand))
        {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        else if (getActivity().getResources().getBoolean(R.bool.is720TabletPortrait))
        {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        else if (getActivity().getResources().getBoolean(R.bool.is720TabletLand))
        {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }

        mMoviesAdapter = new MoviesAdapter(getActivity(), this);

        mMoviesRv = (RecyclerView)rootView.findViewById(R.id.recyclerview_movies);
        mMoviesRv.setHasFixedSize(true);
        mMoviesRv.setLayoutManager(mLayoutManager);
        mMoviesRv.setAdapter(mMoviesAdapter);

        button = true;
        getActivity().setTitle(R.string.popular_movies);
        getActivity().getSupportLoaderManager().initLoader(MOVIE_DB_LOADER, null, this);
        Log.i(TAG,"onCreate");

        return rootView;
    }

    @NonNull
    @Override
    public Loader<List<Movies>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<List<Movies>>(getActivity()) {

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
                if (button) {
                    moviesRequestUrl = NetworkUtils.buildPopularUrl();
                } else {
                    moviesRequestUrl = NetworkUtils.buildTopRatedUrl();
                }

                try {
                    String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                    return MoviesDbJsonUtils.getJson(getActivity(), jsonMoviesResponse);
                }
                catch (Exception e){
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

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        if(pref.getBoolean("favbutton",false)) {
            favItemSelected();
            editor.putBoolean("favbutton", false);
            editor.apply();
        }
    }

    private void loadMoviesData()
    {
        getActivity().getSupportLoaderManager().restartLoader(MOVIE_DB_LOADER, null, this);
    }

    private void favItemSelected()
    {
        mMoviesAdapter.setMovieData(null);
        List<Movies> mov = new ArrayList<Movies>();
        String[] projection =
                {MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                        MovieContract.MovieEntry.COLUMN_TITLE,
                        MovieContract.MovieEntry.COLUMN_OVERVIEW,
                        MovieContract.MovieEntry.COLUMN_RATING,
                        MovieContract.MovieEntry.COLUMN_RELEASE,
                        MovieContract.MovieEntry.COLUMN_IMAGE};

        cursor = getActivity().getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI, projection, null, null, null);
        if (cursor != null) {
            mov = AddCursorToList.getMovList(getActivity(), cursor);
            cursor.close();
        }
        mMoviesAdapter.setMovieData(mov);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu.size() == 0) {
            inflater.inflate(R.menu.main, menu);
        }
    }

    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_popular) {
            button = true;
            mMoviesAdapter.setMovieData(null);
            loadMoviesData();
            getActivity().setTitle(getString(R.string.popular_movies));
            return true;
        }

        else if(id == R.id.action_top_rated)
        {
            button = false;
            mMoviesAdapter.setMovieData(null);
            loadMoviesData();
            getActivity().setTitle(getString(R.string.top_rated_movies));
            return true;
        }
        else if(id == R.id.action_favourites)
        {
            getActivity().getSupportLoaderManager().destroyLoader(MOVIE_DB_LOADER);
            favItemSelected();
            getActivity().setTitle(getString(R.string.favourites));
            editor.putBoolean("favbutton", true);
            editor.apply();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallBack = (OnMovieClickListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnMovieClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public void onClick(Movies movies) {

        if(getResources().getBoolean(R.bool.isTwoPane))
        {
            mCallBack.onMovieSelected(movies);
        }
        else
        {
            Intent intent = new Intent(getActivity(), DetailsActivity.class);
            intent.putExtra("movies", movies);
            startActivity(intent);
        }
    }

}
