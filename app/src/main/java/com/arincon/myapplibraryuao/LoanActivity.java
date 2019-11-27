package com.arincon.myapplibraryuao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class LoanActivity extends AppCompatActivity {

    private EditText _etResNameByLoan;
    private Spinner _spResNameByLoan;
    private Button _btnResNameByLoan, _btnBackByLoan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan);

        _etResNameByLoan = findViewById(R.id.etResNameByLoan);
        _spResNameByLoan = findViewById(R.id.spResByLoan);
        _btnResNameByLoan = findViewById(R.id.btnResByLoan);

        final String user_id = getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name");
        final String last_name = getIntent().getStringExtra("last_name");
        final String email = getIntent().getStringExtra("email");
        final String role = getIntent().getStringExtra("role");
        final String token = getIntent().getStringExtra("token");

        String [] opciones = {"Libro", "Revista"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        _spResNameByLoan.setAdapter(adapter);
    }
}
