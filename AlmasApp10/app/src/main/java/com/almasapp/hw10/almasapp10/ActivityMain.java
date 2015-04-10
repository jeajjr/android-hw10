package com.almasapp.hw10.almasapp10;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class ActivityMain extends ActionBarActivity implements FragmentRecyclerView.OnItemClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new FragmentRecyclerView())
                .commit();
    }

    @Override
    public void onItemClick(String id) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, FragmentMovieDetail.newInstance(id))
                .addToBackStack(null)
                .commit();
    }
}
