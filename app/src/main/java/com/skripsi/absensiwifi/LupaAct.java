package com.skripsi.absensiwifi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LupaAct extends AppCompatActivity {

    private static final String TAG = LupaAct.class.getSimpleName();
    private DataService service;

    private EditText etNIK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa);

        service = ServiceGenerator.createBaseService(this, DataService.class);
        initListener();
    }

    private void initListener() {
        Button btnResetPassword = findViewById(R.id.btn_reset);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNIK = findViewById(R.id.edt_nik);
                String nik = etNIK.getText().toString();

                if(isEmpty(nik))
                    etNIK.setError("NIK tidak boleh kosong");
                else
                    ResetPassword(nik);
            }
        });
    }

    private void ResetPassword(final String nik) {
        Call<BaseResponse> call = service.apiResetPassword(nik);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    BaseResponse ResetObject = response.body();
                    String returnedResponse = ResetObject.status;

                    if(returnedResponse.trim().equals("true")) {
                        Toast.makeText(LupaAct.this, "Password berhasil direset", Toast.LENGTH_SHORT).show();

                        Intent HomeActivity = new Intent(LupaAct.this, HomeAct.class);
                        startActivity(HomeActivity);
                    }else{
                        Toast.makeText(LupaAct.this, "NIK tidak ditemukan", Toast.LENGTH_SHORT).show();
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
