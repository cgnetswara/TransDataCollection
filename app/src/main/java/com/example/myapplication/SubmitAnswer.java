package com.example.myapplication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SubmitAnswer extends AsyncTask<String, Void, JSONObject> {

    OnTaskCompleted onTaskCompleted = null;

    public SubmitAnswer(OnTaskCompleted onTaskCompleted) {
        this.onTaskCompleted = onTaskCompleted;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String response = "";

        try {

            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            int data = reader.read();

            while(data != -1){
                response += (char) data;
                data = reader.read();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        onTaskCompleted.onTaskCompleted(jsonObject);
    }
}
