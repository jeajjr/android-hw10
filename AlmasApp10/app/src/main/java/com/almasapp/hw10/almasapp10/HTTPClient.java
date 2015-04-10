package com.almasapp.hw10.almasapp10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

public class HTTPClient {
    private static final String TAG = "HTTPClient";

    private static final String BASE_URL = "http://www.example.com/movies/";

    public static String getMovieList() {
        return downloadJSON(BASE_URL);
    }

    public static String getMovieDetail(String requestString) {
        return downloadJSON(BASE_URL + "id/" + requestString);
    }

    public static String downloadJSON(String url) {
        String json=null, line;

        InputStream stream = getHttpConnection(url);
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            try {
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                json = out.toString();
            } catch (IOException ex) {
                Log.d("MyDebugMsg", "IOException in downloadJSON()");
                ex.printStackTrace();
            }
        }
        return json;
    }

    // Makes HttpURLConnection and returns InputStream
    public static InputStream getHttpConnection(String urlString) {
        InputStream stream = null;
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }  catch (UnknownHostException e1) {
            Log.d("MyDebugMsg", "UnknownHostexception in getHttpConnection()");
            e1.printStackTrace();
        } catch (Exception ex) {
            Log.d("MyDebugMsg", "Exception in getHttpConnection()");
            ex.printStackTrace();
        }
        return stream;
    }

    public static String getData(String requestString) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL(requestString)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ( (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();
            return buffer.toString();
        }
        catch(Throwable t) {
            Log.d(TAG, "Throwable caught: ", t);
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
