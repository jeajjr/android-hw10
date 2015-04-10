package com.almasapp.hw10.almasapp10;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentMovieDetail extends Fragment {
    final String TAG = "MovieDetailFragment";

    static String ARG_MOVIE_ID = "id";

    private String movieID;

    public FragmentMovieDetail() {
        // Required empty public constructor
    }

    public static FragmentMovieDetail newInstance(String movie) {
        FragmentMovieDetail fragment = new FragmentMovieDetail();

        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_ID, movie);
        fragment.setArguments(args);

        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        movieID = getArguments().getString(ARG_MOVIE_ID);

        TextView title = (TextView) rootView.findViewById(R.id.textViewMovieDetailTitle);
        TextView year = (TextView) rootView.findViewById(R.id.textViewMovieDetailYear);
        TextView length = (TextView) rootView.findViewById(R.id.textViewMovieDetailLength);
        TextView stars = (TextView) rootView.findViewById(R.id.textViewMovieDetailStars);
        TextView director = (TextView) rootView.findViewById(R.id.textViewMovieDetailDirector);
        ImageView cover = (ImageView) rootView.findViewById(R.id.imageViewMovieDetailCover);
        RatingBar rating = (RatingBar) rootView.findViewById(R.id.ratingBarMovieDetailRating);
        TextView description = (TextView) rootView.findViewById(R.id.textViewMovieDetailDescription);


        (new MovieDetailDownloader(getActivity(), title, year, length, stars, director, cover,
                rating, description)).execute(movieID);

        return rootView;
    }


}