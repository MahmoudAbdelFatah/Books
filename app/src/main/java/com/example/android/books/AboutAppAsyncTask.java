package com.example.android.books;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by mahmoudabdelfatahabd on 28-Aug-17.
 */
public class AboutAppAsyncTask extends AsyncTask<String, Void, String> {
    String apiKey;
    StringBuffer stringBuffer;

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected String doInBackground(String... params) {
        apiKey = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        try {
            Uri builder = Uri.parse(apiKey);
            URL url = new URL(builder.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = null;
            inputStream = urlConnection.getInputStream();
            stringBuffer = new StringBuffer();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str + "\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlConnection.disconnect();
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
}