package com.arincon.myapplibraryuao;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

public class RecourceByZoneActivity extends AppCompatActivity {

    private Spinner _spResByZone;
    private EditText _etResNameByZone;
    private Button _btnResNameByZone, _btnBackByZone;

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mapbox.getInstance(this, "pk.eyJ1IjoiYWZyaW5jb24iLCJhIjoiY2syN3l3a3hvMDhheDNtbnlxejNjN3pubSJ9.j0rsO22CXylLm-M-3emIlQ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_by_zone);

        _etResNameByZone = findViewById(R.id.etResNameByZone);
        _spResByZone = findViewById(R.id.spResByZone);
        _btnResNameByZone = findViewById(R.id.btnResByZone);
        _btnBackByZone = findViewById(R.id.btnBackByZone);


        final String user_id = getIntent().getStringExtra("id");
        final String name = getIntent().getStringExtra("name");
        final String last_name = getIntent().getStringExtra("last_name");
        final String email = getIntent().getStringExtra("email");
        final String role = getIntent().getStringExtra("role");
        final String token = getIntent().getStringExtra("token");

        String [] opciones = {"Libro", "Revista"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, opciones);
        _spResByZone.setAdapter(adapter);


        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                    }
                });
            }
        });

        _btnResNameByZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String select = _spResByZone.getSelectedItem().toString();
                String name = _etResNameByZone.getText().toString();

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

        _btnBackByZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecourceByZoneActivity.this, MenuActivity.class);
                i.putExtra("user_id", user_id);
                i.putExtra("name", name);
                i.putExtra("last_name", last_name);
                i.putExtra("email", email);
                i.putExtra("role", role);
                i.putExtra("token", token);
                startActivity(i);
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

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
