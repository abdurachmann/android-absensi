package com.skripsi.absensiwifi;

import android.content.Context;
import android.content.Intent;
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

    private static final String TAG = ProfileAct.class.getSimpleName();

    private DataService service;
    public TextView tvNik;
    public TextView tvNama;
    public TextView tvTanggallahir;
    public TextView tvAlamat;

    public String st_nik;
    public String st_nama;
    public String st_tanggallahir;
    public String st_alamat;

    private Session session;
    private Context cntx;

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

        // Get Data from API
        initViews();

        // Session
        session = new Session(cntx);
        String nik = session.getNik();

        // Initialization adapter
        service = ServiceGenerator.createBaseService(this, DataService.class);

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
                        st_nik = ProfileObject.getString("nik");
                        st_nama = ProfileObject.getString("nama");
                        st_tanggallahir = ProfileObject.getString("tanggallahir");
                        st_alamat = ProfileObject.getString("alamat");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    tvNik.setText(st_nik);
                    tvNama.setText(st_nama);
                    tvTanggallahir.setText(st_tanggallahir);
                    tvAlamat.setText(st_alamat);
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
