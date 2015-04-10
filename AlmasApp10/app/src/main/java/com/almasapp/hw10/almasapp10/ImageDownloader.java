package com.almasapp.hw10.almasapp10;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewWeakReference;

    private static LruCache<String, Bitmap> imageCache;

    public ImageDownloader(ImageView imageView) {
        imageViewWeakReference = new WeakReference<>(imageView);

        if (imageCache == null) {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;

            Log.d("ImageDownloader", "cacheSize " + cacheSize);

            imageCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0];
        Bitmap bitmap = imageCache.get(url);

        if (bitmap == null) {
            bitmap = HTTPClient.getMovieImage(url);
            imageCache.put(url, bitmap);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            final ImageView imageView = imageViewWeakReference.get();

            if (imageView != null)
                imageView.setImageBitmap(bitmap);
        }
    }
}
