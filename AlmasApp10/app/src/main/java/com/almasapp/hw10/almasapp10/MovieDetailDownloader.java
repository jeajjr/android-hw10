package com.almasapp.hw10.almasapp10;


import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

public class MovieDetailDownloader extends AsyncTask<String, Void, HashMap> {
    private static final String TAG = "MovieDataDownloader";

    private  final WeakReference<Activity> activityWeakReference;
    private final WeakReference<TextView> titleWeakReference;
    private final WeakReference<TextView> yearWeakReference;
    private final WeakReference<TextView> lengthWeakReference;
    private final WeakReference<TextView> starsWeakReference;
    private final WeakReference<TextView> directorWeakReference;
    private final WeakReference<ImageView> coverWeakReference;
    private final WeakReference<RatingBar> ratingWeakReference;
    private final WeakReference<TextView> descriptionWeakReference;


    public MovieDetailDownloader(Activity activity, TextView title, TextView year, TextView length,TextView stars,
                                 TextView director, ImageView cover, RatingBar rating, TextView description) {

        activityWeakReference = new WeakReference<>(activity);
        titleWeakReference = new WeakReference<>(title);
        yearWeakReference = new WeakReference<>(year);
        lengthWeakReference = new WeakReference<>(length);
        starsWeakReference = new WeakReference<>(stars);
        directorWeakReference = new WeakReference<>(director);
        coverWeakReference = new WeakReference<>(cover);
        ratingWeakReference = new WeakReference<>(rating);
        descriptionWeakReference = new WeakReference<>(description);
    }

    @Override
    protected HashMap doInBackground(String... params) {
        Log.d(TAG, "doInBackground");

        String data = HTTPClient.getMovieDetail(params[0]);

        try {
            if (data != null) {
                JSONObject jsonObject = (new JSONArray(data)).getJSONObject(0);

                if (jsonObject != null) {

                    HashMap<String, ?> movie = new HashMap<>();

                    ((HashMap<String, String>) movie).put("description",
                            jsonObject.getString("description"));
                    ((HashMap<String, String>) movie).put("stars",
                            jsonObject.getString("stars"));
                    ((HashMap<String, String>) movie).put("name",
                            jsonObject.getString("name"));
                    ((HashMap<String, String>) movie).put("length",
                            jsonObject.getString("length"));
                    ((HashMap<String, String>) movie).put("image",
                            jsonObject.getString("image"));
                    ((HashMap<String, String>) movie).put("year",
                            jsonObject.getString("year"));
                    ((HashMap<String, Double>) movie).put("rating",
                            jsonObject.getDouble("rating"));
                    ((HashMap<String, String>) movie).put("director",
                            jsonObject.getString("director"));
                    ((HashMap<String, String>) movie).put("url",
                            jsonObject.getString("url"));

                    return movie;
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage(), e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(HashMap movie) {
        Log.d(TAG, "onPostExecute");

        if (movie != null) {

            final Activity activity = activityWeakReference.get();
            final TextView title = titleWeakReference.get();
            final TextView year = yearWeakReference.get();
            final TextView length = lengthWeakReference.get();
            final TextView stars = starsWeakReference.get();
            final TextView director = directorWeakReference.get();
            final ImageView cover = coverWeakReference.get();
            final RatingBar rating = ratingWeakReference.get();
            final TextView description = descriptionWeakReference.get();

            title.setText(movie.get("name").toString());
            year.setText(movie.get("year").toString());
            length.setText(movie.get("length").toString());
            stars.setText(activity.getResources().getText(R.string.movies_stars) + " " + movie.get("stars").toString());
            director.setText(activity.getResources().getText(R.string.movies_director) + " " + movie.get("director").toString());

            (new ImageDownloader(cover)).execute(movie.get("url").toString());

            rating.setRating( (int) Double.parseDouble(movie.get("rating").toString()) + 1 );
            description.setText(movie.get("description").toString());
        }
    }
}