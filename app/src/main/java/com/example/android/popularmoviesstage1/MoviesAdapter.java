package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movies> mMovieList;
    Context mContext;
    private final MoviesAdapterOnClickHandler mClickHandler;

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler clickHandler)
    {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public interface MoviesAdapterOnClickHandler
    {
        void onClick(String title,String plot, Double rating, String release, String poster);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView moviePosterImage;

        public MovieViewHolder(View itemView) {
            super(itemView);

            moviePosterImage = (ImageView)itemView.findViewById(R.id.movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            Movies movie = mMovieList.get(adapterPosition);

            String mTitle = movie.getmTitle();
            String mOverview = movie.getmOverview();
            Double rating = movie.getmRating();
            String release = movie.getmReleaseDate();
            String moviePoster = movie.getmPosterId();

            mClickHandler.onClick(mTitle, mOverview, rating, release, moviePoster);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachImmediately);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        Movies movie = mMovieList.get(position);
        String imageName = movie.getmPosterId();
        imageName = imageName.replace("/", "");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185")
                .appendPath(imageName);

        String url = builder.build().toString();

        Glide.with(mContext).load(url).into(holder.moviePosterImage);

    }

    @Override
    public int getItemCount() {
        if(mMovieList == null)
        {
            return 0;
        }
        else
        {
            return mMovieList.size();
        }
    }

    public void setMovieData(List<Movies>movieList)
    {
        mMovieList = movieList;
        notifyDataSetChanged();
    }
}
