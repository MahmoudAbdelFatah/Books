package com.example.android.books;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutApp extends AppCompatActivity {
    TextView descriptionTextView, developerTextView, emailTextView;
    String aboutAppData, description, developer, email;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);
        descriptionTextView = (TextView) findViewById(R.id.descriptionId);
        developerTextView = (TextView) findViewById(R.id.developerId);
        emailTextView = (TextView) findViewById(R.id.emailId);
        scrollView = (ScrollView) findViewById(R.id.scroll_id);
        description = "Description : ";
        developer = "Developer : ";
        email = "email : ";
        aboutAppData = getIntent().getExtras().getString("data");

        try {
            GetData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray("scrollState",
                new int[]{scrollView.getScrollX(), scrollView.getScrollY()});
    }
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int[] position = savedInstanceState.getIntArray("scrollState");
        if (position != null)
            scrollView.post(new Runnable() {
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
    }

    public void GetData() throws JSONException {
        JSONObject jsonObject = new JSONObject(aboutAppData);
        description += jsonObject.optString("description");
        developer += jsonObject.optString("developer");
        email += jsonObject.optString("email");
        descriptionTextView.setText(description);
        developerTextView.setText(developer);
        emailTextView.setText(email);
    }
}
