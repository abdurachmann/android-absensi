package com.skripsi.absensiwifi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.io.IOException;

public class LoginAct extends AppCompatActivity implements View.OnClickListener, Runnable {
    private Button btn_login;
    private EditText edt_username, edt_password;

    OkHttpClient client = new OkHttpClient();
    private final String loginurl = "http://192.168.0.2/absensi/api/Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        new Thread(this).start();
    }

    @Override
    public void run() {
        RequestBody formBody = new FormEncodingBuilder()
                .add("username", edt_username.getText().toString())
                .add("password", edt_password.getText().toString())
                .build();
        Request request = new  Request.Builder()
                .url(loginurl)
                .post(formBody)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }
            System.out.println(response.body().string());
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
