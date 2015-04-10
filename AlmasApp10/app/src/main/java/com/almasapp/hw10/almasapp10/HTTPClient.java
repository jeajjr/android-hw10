package com.almasapp.hw10.almasapp10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by José Ernesto on 25/03/2015.
 */
public class HTTPClient {
    private static final String TAG = "HTTPClient";

    private static final String BASE_URL = "http://www.cis.syr.edu/~wedu/Teaching/android/Labs/json/";

    public static String getMovieList(String requestString) {
        return getData(BASE_URL + requestString + ".json");
    }

    public static String getMovieDetail(String requestString) {
        return getData(BASE_URL + requestString + ".json");
    }

    public static String getData(String requestString) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) ( new URL(requestString)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }

    public static Bitmap getMovieImage(String url) {
        HttpURLConnection con = null;
        InputStream is = null;
        Bitmap bitmap = null;

        try {
            con = (HttpURLConnection) (new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            is = con.getInputStream();

            if (is != null) {
                bitmap = BitmapFactory.decodeStream(is);
            }

            is.close();
            con.disconnect();
            return bitmap;
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }
}
