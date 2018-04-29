package com.example.android.popularmoviesstage1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.pojo.Reviews;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>{

    private List<Reviews> mReviewsList;
    Context mContext;

    public ReviewsAdapter(Context context)
    {
        mContext = context;
    }
    class ReviewViewHolder extends RecyclerView.ViewHolder
    {
        TextView movieReviewAuthor;
        TextView movieReviewContent;
        public ReviewViewHolder(View itemView)
        {
            super(itemView);
            movieReviewAuthor = (TextView)itemView.findViewById(R.id.textViewAuthor);
            movieReviewContent = (TextView)itemView.findViewById(R.id.textViewContent);
        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.review_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachImmediately);
        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        Reviews review = mReviewsList.get(position);
        String authorName = review.getmAuthor();
        String reviewContent = review.getmContent();

        holder.movieReviewAuthor.setText(authorName);
        holder.movieReviewContent.setText(reviewContent);
    }

    @Override
    public int getItemCount() {
        if(mReviewsList == null)
        {
            return 0;
        }
        else
        {
            return mReviewsList.size();
        }
    }

    public void setReviewData(List<Reviews>reviewsList)
    {
        mReviewsList = reviewsList;
        notifyDataSetChanged();
    }
}
