package com.example.android.popularmoviesstage1.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.popularmoviesstage1.R;
import com.example.android.popularmoviesstage1.pojo.Movies;
import com.example.android.popularmoviesstage1.pojo.Trailers;

import java.util.List;

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder>{

    private List<Trailers> mTrailerList;
    Context mContext;
    private final TrailersAdapterOnClickHandler mClickHandler;

    public interface TrailersAdapterOnClickHandler
    {
        void onClick(Trailers trailer);
    }

    public TrailersAdapter(Context context, TrailersAdapterOnClickHandler clickHandler)
    {
        mContext = context;
        mClickHandler = clickHandler;

    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView trailerImage;
        TextView trailerDescription;

        public TrailerViewHolder(View itemView)
        {
            super(itemView);
            trailerImage = (ImageView)itemView.findViewById(R.id.imageViewTrailer);
            trailerDescription = (TextView)itemView.findViewById(R.id.textViewTrailerDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailers trailer = mTrailerList.get(adapterPosition);
            mClickHandler.onClick(trailer);
        }
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachImmediately);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {

        Trailers trailer = mTrailerList.get(position);
         String trailerKey = trailer.getmTrailerKey();
         String trailerDescription = trailer.getmTrailerDescription();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("img.youtube.com")
                .appendPath("vi")
                .appendPath(trailerKey)
                .appendPath("maxresdefault.jpg");

        String url = builder.build().toString();

        Glide.with(mContext).load(url).into(holder.trailerImage);
        holder.trailerDescription.setText(trailerDescription);
    }

    @Override
    public int getItemCount() {
        if(mTrailerList == null)
        {
            return 0;
        }
        else
        {
            return mTrailerList.size();
        }
    }
    public void setTrailerData(List<Trailers>trailerList)
    {
        mTrailerList = trailerList;
        notifyDataSetChanged();
    }
}
