package com.skripsi.absensiwifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileAct extends AppCompatActivity {
    ImageView btn_back;

    private static final String TAG = ProfileAct.class.getSimpleName();

    private DataService service;
    private TextView tvNik;
    private TextView tvNama;
    private TextView tvTanggallahir;
    private TextView tvAlamat;
    
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

        String nik = "P0001";

        // Initialization adapter
        service = ServiceGenerator.createBaseService(this, DataService.class);

        loadData(nik);
    }

    private void loadData(String nik) {
        Call<BaseResponse> call = service.apiProfile(nik);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.code() == 200) {
                    BaseResponse ProfileObject = (BaseResponse) response.body().getData();

                    String dataNIK = ProfileObject.nik;
                    String dataNama = ProfileObject.nama;
                    String dataTanggalLahir = ProfileObject.tanggallahir;
                    String dataAlamat = ProfileObject.alamat;

                    tvNik.setText(dataNIK);
                    tvNama.setText(dataNama);
                    tvTanggallahir.setText(dataTanggalLahir);
                    tvAlamat.setText(dataAlamat);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void initViews() {
        TextView tvNik = (TextView) findViewById(R.id.tv_nik);
        TextView tvNama = (TextView) findViewById(R.id.tv_nama);
        TextView tvTanggallahir = (TextView) findViewById(R.id.tv_tanggal_lahir);
        TextView tvAlamat = (TextView) findViewById(R.id.tv_alamat);
    }
}
