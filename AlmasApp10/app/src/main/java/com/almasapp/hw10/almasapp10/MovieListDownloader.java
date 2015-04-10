package com.almasapp.hw10.almasapp10;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieListDownloader extends AsyncTask<Void, Void, ArrayList> {
    private static final String TAG = "MovieDataDownloader";

    private final WeakReference<Activity> parentWeakReference;
    private final WeakReference<ArrayList> arrayListWeakReference;
    private final WeakReference<MyRecyclerViewAdapter> adapterWeakReference;

    public MovieListDownloader(Activity activity, ArrayList arrayList, MyRecyclerViewAdapter adapter) {

        parentWeakReference = new WeakReference<>(activity);
        arrayListWeakReference = new WeakReference<>(arrayList);
        adapterWeakReference = new WeakReference<>(adapter);
    }

    @Override
    protected ArrayList doInBackground(Void... params) {
        Log.d(TAG, "doInBackground");

        ArrayList<HashMap> arrayList = new ArrayList<>();
        String data = null;

        if (checkInternetAccess()) {
            Log.d(TAG, "Got internet connection");
            data = HTTPClient.getMovieList();
        }
        else
            Log.d(TAG, "No internet connection");

        try {
            if (data != null) {
                JSONArray jsonArray = new JSONArray(data);

                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject movieObj = (JSONObject) jsonArray.get(i);

                        HashMap<String, ?> movie = new HashMap<>();
                        ((HashMap<String, String>) movie).put("name",
                                movieObj.getString("name"));
                        ((HashMap<String, String>) movie).put("description",
                                movieObj.getString("description"));
                        ((HashMap<String, Double>) movie).put("rating",
                                movieObj.getDouble("rating"));
                        ((HashMap<String, String>) movie).put("id",
                                movieObj.getString("id"));
                        ((HashMap<String, String>) movie).put("url",
                                movieObj.getString("url"));

                        arrayList.add(movie);
                    }

                    return arrayList;
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(ArrayList result) {
        Log.d(TAG, "onPostExecute");

        ArrayList arrayList = arrayListWeakReference.get();

        if (arrayList != null && result != null) {
            arrayList.clear();
            arrayList.addAll(result);
            final MyRecyclerViewAdapter adapter = adapterWeakReference.get();
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Check if internet access is available.
     * @return true if available, false otherwise.
     */
    private boolean checkInternetAccess() {
        final Activity activity = parentWeakReference.get();

        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }
}
