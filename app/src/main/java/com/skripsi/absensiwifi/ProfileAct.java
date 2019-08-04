package com.skripsi.absensiwifi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAct extends AppCompatActivity {
    ImageView btn_back;
    TextView ubah_pass;

    private static final String TAG = ProfileAct.class.getSimpleName();

    private DataService service;
    public TextView tvNik;
    public TextView tvNama;
    public TextView tvTanggallahir;
    public TextView tvAlamat;

    public String strNik;
    public String strNama;
    public String strTanggalLahir;
    public String strAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohome = new Intent(ProfileAct.this, HomeAct.class);
                startActivity(gotohome);
                finish();
            }
        });

        ubah_pass = findViewById(R.id.ubah_pass);
        ubah_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoubahpassword = new Intent(ProfileAct.this, UbahAct.class);
                startActivity(gotoubahpassword);
                finish();
            }
        });

        // Get Data from API
        initViews();

        // Initialization adapter
        service = ServiceGenerator.createBaseService(this, DataService.class);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode
        String nik = pref.getString("nik", "");

        loadData(nik);
    }

    private void loadData(String nik) {
        Call<BaseResponse> call = service.apiProfile(nik);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {

                    try {
                        JSONObject ProfileObject = new JSONObject(new Gson().toJson(response.body().getData()));
                        strNik = ProfileObject.getString("nik");
                        strNama = ProfileObject.getString("nama");
                        strTanggalLahir = ProfileObject.getString("tanggallahir");
                        strAlamat = ProfileObject.getString("alamat");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    tvNik.setText(strNik);
                    tvNama.setText(strNama);
                    tvTanggallahir.setText(strTanggalLahir);
                    tvAlamat.setText(strAlamat);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        tvNik = findViewById(R.id.tv_nik);
        tvNama = findViewById(R.id.tv_nama);
        tvTanggallahir = findViewById(R.id.tv_tanggal_lahir);
        tvAlamat = findViewById(R.id.tv_alamat);
    }
}
