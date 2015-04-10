package com.almasapp.hw10.almasapp10;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Map;

public class FragmentRecyclerView extends Fragment {
    static String TAG = "FragmentRecyclerView";
    static String ARG_MOVIE_LIST = "movie_list";

    private ArrayList<Map<String, ?>> movieData;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;
    private RecyclerView moviesRecyclerView;

    private OnItemClickedListener mListener;

    public interface OnItemClickedListener {
        public void onItemClick(String id);
    }

    public FragmentRecyclerView() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            movieData = (ArrayList<Map<String, ?>>) bundle.get(ARG_MOVIE_LIST);
        }
        else {
            movieData = new ArrayList<>();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnItemClickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemClickedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        setHasOptionsMenu(true);

        moviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        moviesRecyclerView.setHasFixedSize(true);


        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), movieData);

        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "list item clicked");

                mListener.onItemClick((String) movieData.get(position).get("id"));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "list item long clicked");
            }
        });

        moviesRecyclerView.setAdapter(myRecyclerViewAdapter);

        (new MovieListDownloader(getActivity(), movieData, myRecyclerViewAdapter)).execute();

        return rootView;
    }
}