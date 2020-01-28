package com.example.alfreddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LogIn extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        getSupportActionBar().setTitle("LogIn");
    }

    public void btn_sign_up(View view) {
        startActivity(new Intent(getApplicationContext(),SignUp.class));
    }

    public void btn_lang_options(View view) {
        startActivity(new Intent(getApplicationContext(), LangAdapter.class));
    }
}
