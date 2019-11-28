package com.arincon.myapplibraryuao;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResourceByQRActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button _btnLoanQr, _btnScanQr;
    private TextView _tvResourceName, _tvResourceISBN, _tvResourceEdition, _tvResourceAuthor, _tvResourceCampus, _tvResourceType;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    String id;
    String name;
    String isbn;
    String edition;
    String author;
    String campus;
    String type;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_by_qr);

        user_id = getIntent().getStringExtra("user_id");
        final String user_name = getIntent().getStringExtra("name");
        final String last_name = getIntent().getStringExtra("last_name");
        final String email = getIntent().getStringExtra("email");
        final String role = getIntent().getStringExtra("role");
        final String token = getIntent().getStringExtra("token");

        _tvResourceName = findViewById(R.id.tvResourceName);
        _tvResourceISBN = findViewById(R.id.tvResourceISBN);
        _tvResourceEdition = findViewById(R.id.tvResourceEdition);
        _tvResourceAuthor = findViewById(R.id.tvResourceAuthor);
        _tvResourceCampus = findViewById(R.id.tvResourceCampus);
        _tvResourceType = findViewById(R.id.tvResourceType);


        _btnScanQr = findViewById(R.id.btnScanQr);
        _btnLoanQr = findViewById(R.id.btnLoanQR);

        _btnScanQr.setOnClickListener(this);
        _btnLoanQr.setOnClickListener(this);
    }

    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if(scanningResult != null) {
            String scanContent = scanningResult.getContents();
            StringTokenizer t = new StringTokenizer(scanContent, "*");
            id = t.nextToken();
            name = t.nextToken();
            isbn = t.nextToken();
            edition = t.nextToken();
            author = t.nextToken();
            campus = t.nextToken();
            type = t.nextToken();

            _tvResourceName.setText(name);
            _tvResourceISBN.setText(isbn);
            _tvResourceEdition.setText(edition);
            _tvResourceAuthor.setText(author);
            _tvResourceCampus.setText(campus);
            _tvResourceType.setText(type);

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "no se han recibido datos del escaneo", Toast.LENGTH_LONG);
            toast.show();
        }

    }

    @Override
    public void onClick(View v) {
        String userId = this.user_id;

        if(v.getId() == R.id.btnScanQr) {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }

        if(v.getId() == R.id.btnLoanQR) {
            String resourceId = id;
            String campusId = null;
            if (campus.equals("Valle del lili")) {
                campusId = "1";
            } else if (campus.equals("San fernando")) {
                campusId = "2";
            } else if (campus.equals("Proviencia")) {
                campusId = "3";
            }

            if (resourceId == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "no se han recibido datos del escaneo", Toast.LENGTH_LONG);
                toast.show();
            } else {
                try {
                    postRequest(userId, resourceId, campusId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void postRequest(String userId, String resourceId, String campusId) throws IOException {
        String url = "https://libraryuao.herokuapp.com/api/loan";
        OkHttpClient client = new OkHttpClient();

        JSONObject postdata = new JSONObject();

        try {
            postdata.put("user_id", userId);
            postdata.put("book_id", resourceId);
            postdata.put("campus_id", campusId);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, postdata.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        Call call = client.newCall(request);

    }
}
