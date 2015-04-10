package com.almasapp.hw10.almasapp10;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Map<String, ?>> mDataSet;
    private Context context;
    OnItemClickListener onItemClickListener;

    public MyRecyclerViewAdapter(Context context, List<Map<String, ?>> mDataSet) {
        this.mDataSet = mDataSet;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView vIcon;
        public TextView vTitle;
        public TextView vDescription;
        public RatingBar ratingBar;

        public ViewHolder(View v) {
            super(v);
            vIcon = (ImageView) v.findViewById(R.id.imageViewMoviesImage);
            vTitle = (TextView) v.findViewById(R.id.textViewMoviesTitle);
            vDescription = (TextView) v.findViewById(R.id.textViewMoviesDescription);
            ratingBar = (RatingBar) v.findViewById(R.id.ratingBarMovie);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, getPosition());
                    }
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemLongClick(v, getPosition());
                    }
                    return true;
                }
            });
        }

        public void bindMovieData (Map<String, ?> movie) {
            vTitle.setText((String) movie.get("name"));
            vDescription.setText((String) movie.get("description"));
            ratingBar.setRating((int) Double.parseDouble(movie.get("rating").toString()) + 1);

            (new ImageDownloader(vIcon)).execute((String) movie.get("url"));
        }
    }

    /**
     * OnItemClickListener interface for clicks on list items.
     * This design pattern allows the listener actions to be defined
     * outside the adapter.
     */
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.onItemClickListener = mItemClickListener;
    }

    @Override
    public MyRecyclerViewAdapter.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_row_linear, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder (ViewHolder holder, int position) {
        Map<String, ?> movie = mDataSet.get(position);
        holder.bindMovieData(movie);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }


}