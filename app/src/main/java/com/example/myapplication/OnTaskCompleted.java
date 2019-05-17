package com.example.myapplication;

import org.json.JSONObject;

// It is the interface to make the SubmitAnswer class work on UI thread. Implemented in MainActivity
public interface OnTaskCompleted {
    void onTaskCompleted(JSONObject s);
}
