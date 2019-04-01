package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
    String BASE_URL = "http://192.168.43.235:8000/";
    JSONArray questionIdList;
    JSONObject Questionresponse;
    TextView questionTextView;
    int current_question = 0;

    class QuestionFetch extends AsyncTask<String, Void, JSONObject>{

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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);

        QuestionFetch questionFetch = new QuestionFetch();
        try {

            Questionresponse = questionFetch.execute(BASE_URL+"getTen").get();
            questionIdList = Questionresponse.names();
            questionTextView.setText(Questionresponse.getString(questionIdList.getString(current_question)));

            //Toast.makeText(this, questionIdList.toString(), Toast.LENGTH_LONG).show();

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onPressSubmit(View view) {

        String answer = answerEditText.getText().toString();

        if (answer.length() == 0){
            Toast.makeText(this, "Please enter a answer", Toast.LENGTH_SHORT).show();
        }
        else{
            try {
                String url = BASE_URL+"storeAnswer/" + questionIdList.getString(current_question) + "/" + answer;
                Log.i("urll", url);
                SubmitAnswer submitAnswer = new SubmitAnswer();
                submitAnswer.execute(url).get();
                Toast.makeText(this, "Answer Submitted", Toast.LENGTH_SHORT).show();
                current_question += 1;
                questionTextView.setText(Questionresponse.getString(questionIdList.getString(current_question)));
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


}
