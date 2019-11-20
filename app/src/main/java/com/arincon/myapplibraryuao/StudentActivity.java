package com.arincon.myapplibraryuao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class StudentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        String id = getIntent().getStringExtra("id");
        String name = getIntent().getStringExtra("name");
        String last_name = getIntent().getStringExtra("last_name");
        String program = getIntent().getStringExtra("program");
        String faculty = getIntent().getStringExtra("faculty");
        String role = getIntent().getStringExtra("role");
    }
}
