package com.arincon.myapplibraryuao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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


        _btnResNameByLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String select = _spResNameByLoan.getSelectedItem().toString();
                String name = _etResNameByLoan.getText().toString();

                if(select == "Libro") {
                    select = "book";
                } else if(select == "Revista") {
                    select = "magazine";
                }

                try {
                    getResourcesByZone(select, name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void getResourcesByZone(String type, String name) throws IOException {
        String url = "https://libraryuao.herokuapp.com/api/searchresourcesbycampus";
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();

        builder.addQueryParameter("name", name);
        builder.addQueryParameter("type", type);

        String urlFinal = builder.build().toString();

        Request request = new Request.Builder()
                .url(urlFinal)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    try (ResponseBody responseBody = response.body()) {
                        String nn = responseBody.string();
                        System.out.println(nn);
                    }
                }
            }
        });

    }
}
