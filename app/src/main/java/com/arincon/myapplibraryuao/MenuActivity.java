package com.arincon.myapplibraryuao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.arincon.myapplibraryuao.models.User;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MenuActivity extends AppCompatActivity {

    private TextView _tvUserName, _tvEmail, _tvRole;
    private Button _btnLogout, _btnSearchByZone, _btnSearchByQR, _btnLoans;

    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        _tvUserName = findViewById(R.id.tvNameUserData);
        _tvEmail = findViewById(R.id.tvEmailData);
        _tvRole = findViewById(R.id.tvRoleData);

        _btnLogout = findViewById(R.id.btnLogout);
        _btnSearchByZone = findViewById(R.id.btnSearchByZone);
        _btnSearchByQR = findViewById(R.id.btnSearchByQR);
        _btnLoans = findViewById(R.id.btnLoans);


        final String user_id = getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name") + " " + getIntent().getStringExtra("last_name");
        final String name1 = getIntent().getStringExtra("name");
        final String last_name = getIntent().getStringExtra("last_name");
        final String email = getIntent().getStringExtra("email");
        final String role = getIntent().getStringExtra("role");
        final String token = getIntent().getStringExtra("token");

        _tvUserName.setText(name);
        _tvEmail.setText(email);
        _tvRole.setText(role);

        _btnSearchByZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, RecourceByZoneActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("name", name1);
                i.putExtra("last_name", last_name);
                i.putExtra("email", email);
                i.putExtra("role", role);
                i.putExtra("token", token);
                startActivity(i);
            }
        });

        _btnSearchByQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, ResourceByQRActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("name", name1);
                i.putExtra("last_name", last_name);
                i.putExtra("email", email);
                i.putExtra("role", role);
                i.putExtra("token", token);
                startActivity(i);
            }
        });

        _btnLoans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, LoanActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("name", name1);
                i.putExtra("last_name", last_name);
                i.putExtra("email", email);
                i.putExtra("role", role);
                i.putExtra("token", token);
                startActivity(i);
            }
        });

        _btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MenuActivity.this, "Cerrando sesion", Toast.LENGTH_LONG).show();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            OkHttpClient client = new OkHttpClient();

                            String auth = "Bearer " + getIntent().getStringExtra("token");

                            Request request = new Request.Builder()
                                    .header("Authorization", auth)
                                    .url("https://libraryuao.herokuapp.com/api/auth/logout")
                                    .build();

                            Call call = client.newCall(request);

                            Response response = call.execute();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Intent i = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

}
