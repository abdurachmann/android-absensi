package com.skripsi.absensiwifi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class LoginAct extends AppCompatActivity {

    private static final String TAG = LoginAct.class.getSimpleName();
    private DataService service;

    private EditText etNik;
    private EditText etTanggalLahir;
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
        etTanggalLahir = (EditText) findViewById(R.id.edt_tanggal_lahir);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nik = etNik.getText().toString();
                String tanggallahir = etTanggalLahir.getText().toString();

                if(isEmpty(nik))
                    etNik.setError("NIK tidak boleh kosong");
                else
                    Login(nik, tanggallahir);

                if(isEmpty(tanggallahir))
                    etTanggalLahir.setError("Tanggal Lahir tidak boleh kosong");
                else
                    Login(nik, tanggallahir);
            }
        });
    }

    private void Login(String nik, String tanggallahir) {
        Call<BaseResponse> call = service.apiLogin(nik, tanggallahir);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {

                    BaseResponse LoginObject = response.body();
                    String returnedResponse = LoginObject.status;

                    if(returnedResponse.trim().equals("true")) {
                        Toast.makeText(LoginAct.this, "Berhasil", Toast.LENGTH_SHORT).show();

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
