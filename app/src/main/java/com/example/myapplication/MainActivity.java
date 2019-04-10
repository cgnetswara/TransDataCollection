package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.myapplication.SubmitAnswer;

public class MainActivity extends AppCompatActivity {

    EditText answerEditText;
    String BASE_URL = "http://172.16.102.203:8000/";
    JSONArray questionIdList;
    JSONObject questionResponse;
    TextView questionTextView;
    int current_question = 0;
    ProgressBar appStartProgressBar;
    LinearLayout appStartProgressLinearLayout;
    LinearLayout questionLinearLayout;
    TextView headingTextView;
    TelephonyManager telephonyManager;
    String Id;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Id = telephonyManager.getDeviceId();
            //TODO: take care of depreciation of getDEviceId
        }
    }

    class QuestionFetch extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            changeVisibility(0);
            Log.i("loaded", "yes");
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

            if (response.length() > 0){
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
        protected void onPostExecute(JSONObject response) {
            super.onPostExecute(response);
            changeVisibility(1);
            appStartProgressLinearLayout.setVisibility(View.INVISIBLE);

            try {
                questionResponse = response.getJSONObject("words");
                questionIdList = questionResponse.names();
                questionTextView.setText((questionResponse.getString(questionIdList.getString(current_question))));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);
        appStartProgressBar = findViewById(R.id.appStartProgressBar);
        appStartProgressLinearLayout = findViewById(R.id.appStartProgressLineaerLayout);
        questionLinearLayout = findViewById(R.id.questionLinearLayout);
        headingTextView = findViewById(R.id.headingTextView);

        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        // checking for permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        else {
            Id = telephonyManager.getDeviceId();
        }

        QuestionFetch questionFetch = new QuestionFetch();

        questionFetch.execute(BASE_URL+"getTen/" + Id);

        //Toast.makeText(this, questionIdList.toString(), Toast.LENGTH_LONG).show();

    }

    public void onPressSubmit(View view) {

        String answer = answerEditText.getText().toString();

        if (answer.length() == 0){
            Toast.makeText(this, "Please enter a answer", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                String url = BASE_URL+"submitAnswer/" + Id + "/" + questionIdList.getString(current_question) + "/" + answer;
                Log.i("urll", url);
                SubmitAnswer submitAnswer = new SubmitAnswer();
                submitAnswer.execute(url).get();
                Toast.makeText(this, "Answer Submitted", Toast.LENGTH_SHORT).show();
                current_question += 1;
                questionTextView.setText(questionResponse.getString(questionIdList.getString(current_question)));
                answerEditText.setText("");
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    // Function to make make contents on screen visible and invisible before and after downloading the data
    // 1: make visible
    // 0: make invisible

    public void changeVisibility(int flag) {

        if (flag == 1) {
            questionLinearLayout.setVisibility(View.VISIBLE);
            headingTextView.setVisibility(View.VISIBLE);
        } else{
            questionLinearLayout.setVisibility(View.INVISIBLE);
            headingTextView.setVisibility(View.INVISIBLE);
        }
    }

}
