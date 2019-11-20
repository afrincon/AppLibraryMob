package com.arincon.myapplibraryuao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arincon.myapplibraryuao.models.Login;
import com.arincon.myapplibraryuao.models.LoginAccessData;
import com.arincon.myapplibraryuao.models.User;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private EditText _username, _password;
    private Button _login;

    private Login login = new Login();
    private LoginAccessData loginAccessData = new LoginAccessData();
    private User user = new User();

    private static final String TAG = "LoginActivity";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _username = findViewById(R.id.etEmail);
        _password = findViewById(R.id.etPassword);

        _login = findViewById(R.id.btnLogin);

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Login");
                startLogin();
            }
        });
    }

    private void startLogin(){

        if(!validate()) {
            _login.setEnabled(true);
        } else {
            login.setEmail(_username.getText().toString());
            login.setPassword(_password.getText().toString());
            try {
                postRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String token = loginAccessData.getAccess_token();

            if(token == null) {
                onLoginFailed();
            } else {
                try {
                    getRequest();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String id = user.getId();
                String name = user.getName();
                String last_name = user.getLast_name();
                String email = user.getEmail();
                String program = user.getProgram();
                String faculty = user.getFaculty();
                String role = user.getRole();
                String status = user.getStatus();

                if(status != null) {
                    if(status.equals(0)) {
                        Toast.makeText(MainActivity.this, "Usuario deshabilitado comuniquese con la administracion", Toast.LENGTH_LONG).show();
                    } else {
                        if (role == null) {
                            Toast.makeText(MainActivity.this, "Rol invalido", Toast.LENGTH_LONG).show();
                            System.out.println(role);
                        } else {
                            System.out.println(role);
                            if(role.equals("Estudiante")) {
                                Intent i = new Intent(MainActivity.this, StudentActivity.class);
                                i.putExtra("id", id);
                                i.putExtra("name", name);
                                i.putExtra("last_name", last_name);
                                i.putExtra("email", email);
                                i.putExtra("program", program);
                                i.putExtra("faculty", faculty);
                                i.putExtra("role", role);
                                startActivity(i);
                            } else if (role.equals("Profesor")) {
                                Intent i = new Intent(MainActivity.this, ProfessorActivity.class);
                                i.putExtra("id", id);
                                i.putExtra("name", name);
                                i.putExtra("last_name", last_name);
                                i.putExtra("email", email);
                                i.putExtra("program", program);
                                i.putExtra("faculty", faculty);
                                i.putExtra("role", role);
                                startActivity(i);
                            }
                        }
                    }

                }


            }
        }
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login fallido", Toast.LENGTH_LONG).show();
        _login.setEnabled(true);
    }

    private boolean validate(){
        Boolean valid = true;

        String username = _username.getText().toString();
        String password = _password.getText().toString();

        if (username.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            _username.setError("Ingrese una dirección de correo valida");
            valid = false;
        } else {
            _username.setError(null);
        }

        if(password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _password.setError("La contraseña debe de ser entre 4 y 10 caracteres");
            valid = false;
        } else {
            _password.setError(null);
        }
        return valid;
    }

    public void postRequest() throws IOException {
        String url = "https://libraryuao.herokuapp.com/api/auth/login";
        OkHttpClient client = new OkHttpClient();
        final Gson gson = new Gson();
        String json = gson.toJson(login);

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                int res = response.code();
                if (res == 200) {
                    loginAccessData = gson.fromJson(mMessage, LoginAccessData.class);
                }
                //Log.e(TAG, mMessage + " " + res);
            }
        });
    }

    public void getRequest() throws IOException {
        String url = "https://libraryuao.herokuapp.com/api/auth/user";
        OkHttpClient client = new OkHttpClient();

        String token_type = loginAccessData.getToken_type();
        String access_token = loginAccessData.getAccess_token();

        String authorization = String.join(" ", token_type, access_token);

        Request request = new Request.Builder()
                .header("Authorization", authorization)
                .url(url)
                .build();

        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    try (ResponseBody responseBody = response.body()){
                        String nn = responseBody.string();
                        Gson gson = new Gson();
                        user = gson.fromJson(nn, User.class);
                    }
                }
            }
        });
    }
}
