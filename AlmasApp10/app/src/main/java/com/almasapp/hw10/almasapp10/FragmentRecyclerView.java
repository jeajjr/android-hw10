package com.almasapp.hw10.almasapp10;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Map;

public class FragmentRecyclerView extends Fragment {
    static String TAG = "FragmentRecyclerView";
    static String ARG_MOVIE_LIST = "movie_list";

    private ArrayList<Map<String, ?>> moviesList;
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
            moviesList = (ArrayList<Map<String, ?>>) bundle.get(ARG_MOVIE_LIST);
        }
        else {
            moviesList = new ArrayList<>();
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d(TAG, "onCreateOptionsMenu");

        if (menu.findItem(R.id.action_search) == null) {
            inflater.inflate(R.menu.menu_fragment_recycler_view, menu);
            Log.d(TAG, "inflating menu");
        }
        else
            Log.d(TAG, "inflating not necessary");

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchView != null) {
            Log.d(TAG, "setting OnQueryTextListener");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query){
                    Log.d(TAG, "onQueryTextSubmit");

                    try {
                        (new MovieListDownloader(getActivity(), moviesList, myRecyclerViewAdapter)).execute(Double.parseDouble(query));
                    }
                    catch (NumberFormatException e) {
                        Toast.makeText(getActivity(), "Invalid rating number", Toast.LENGTH_SHORT).show();
                    }
                    /*

                    int position = findInMovies(query);

                    if (position != -1)
                        moviesRecyclerView.scrollToPosition(position);
                    else
                        Toast.makeText(getActivity(), getResources().getString(R.string.recycler_view_not_found), Toast.LENGTH_SHORT).show();
                        */

                    return true;
                }
                @Override
                public boolean onQueryTextChange(String query){
                    Log.d(TAG, "onQueryTextChange");
                    return true;
                }
            });
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private int findInMovies (String search) {
        for (int index = 0; index < moviesList.size(); index++)
            if (((String) moviesList.get(index).get("name")).toLowerCase().contains(search.toLowerCase()))
                return index;

        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        setHasOptionsMenu(true);

        moviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.cardList);
        moviesRecyclerView.setHasFixedSize(true);


        moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getActivity(), moviesList);

        myRecyclerViewAdapter.setOnItemClickListener(new MyRecyclerViewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "list item clicked");

                mListener.onItemClick((String) moviesList.get(position).get("id"));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d(TAG, "list item long clicked");
            }
        });

        moviesRecyclerView.setAdapter(myRecyclerViewAdapter);

        (new MovieListDownloader(getActivity(), moviesList, myRecyclerViewAdapter)).execute();

        return rootView;
    }
}