package com.skripsi.absensiwifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class LoginAct extends AppCompatActivity {

    private static final String TAG = LoginAct.class.getSimpleName();
    private DataService service;

    private EditText etNik;
    private EditText etPassword;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initListener();
        service = ServiceGenerator.createBaseService(this, DataService.class);
    }

    @SuppressLint("WrongViewCast")
    private void initListener() {
        btn_login = (Button) findViewById(R.id.btn_login);
        etNik = (EditText) findViewById(R.id.edt_nik);
        etPassword = (EditText) findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nik = etNik.getText().toString();
                String password = etPassword.getText().toString();

                if(isEmpty(nik))
                    etNik.setError("NIK tidak boleh kosong");
                else
                    Login(nik, password);

                if(isEmpty(password))
                    etPassword.setError("Password tidak boleh kosong");
                else
                    Login(nik, password);
            }
        });
    }

    private void Login(final String nik, String password) {
        Call<BaseResponse> call = service.apiLogin(nik, password);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {

                    BaseResponse LoginObject = response.body();
                    String returnedResponse = LoginObject.status;

                    if(returnedResponse.trim().equals("true")) {
                        Toast.makeText(LoginAct.this, "Berhasil", Toast.LENGTH_SHORT).show();

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        try {
                            JSONObject DataLoginObject = new JSONObject(new Gson().toJson(response.body().getData()));
                            editor.putString("nik", DataLoginObject.getString("nik"));
                            editor.putString("nama", DataLoginObject.getString("nama"));
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Intent intent = new Intent(LoginAct.this, HomeAct.class);
                        
                        startActivity(intent);
                    } else {
                        Toast.makeText(LoginAct.this, "Identitas tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }
}
