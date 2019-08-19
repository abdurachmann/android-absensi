package com.skripsi.absensiwifi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class UbahAct extends AppCompatActivity {

    private static final String TAG = UbahAct.class.getSimpleName();
    private DataService service;

    private EditText etPasswordLama;
    private EditText etPasswordBaru;
    private EditText etConfirmPassword;

    ImageView btnBack;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(UbahAct.this, ProfileAct.class);
                startActivity(gotoprofile);
                finish();
            }
        });

        service = ServiceGenerator.createBaseService(this, DataService.class);
        initListener();
    }

    private void initListener() {
        etPasswordLama = findViewById(R.id.pass_lama);
        etPasswordBaru = findViewById(R.id.pass_baru);
        etConfirmPassword = findViewById(R.id.confrim_pass);
        btnSubmit = findViewById(R.id.btn_kirim);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode

            String nik = pref.getString("nik", "");
            String passwordLama = etPasswordLama.getText().toString();
            String passwordBaru = etPasswordBaru.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (isEmpty(passwordLama))
                etPasswordLama.setError("Password tidak boleh kosong");
            else Submit(nik, passwordLama, passwordBaru);

            if (isEmpty(passwordBaru))
                etPasswordBaru.setError("Masukkan password anda");
            else Submit(nik, passwordLama, passwordBaru);

            if (isEmpty(confirmPassword))
                etConfirmPassword.setError("Masukkan password anda");
            else Submit(nik, passwordLama, passwordBaru);

            if (passwordBaru != confirmPassword)
                etConfirmPassword.setError("Masukkan baru tidak sama");
            else Submit(nik, passwordLama, passwordBaru);
            }
        });
    }

    private void Submit(String nik, String passwordlama, String passwordbaru) {
        Call<BaseResponse> call = service.apiChangePassword(nik, passwordlama, passwordbaru);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    BaseResponse ForgetObject = response.body();
                    String returnedResponse = ForgetObject.status;

                    if(returnedResponse.trim().equals("true")) {
                        Toast.makeText(UbahAct.this, "Password berhasil dirubah", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UbahAct.this, "Password tidak berhasil dirubah", Toast.LENGTH_SHORT).show();
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
