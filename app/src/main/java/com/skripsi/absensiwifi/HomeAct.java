package com.skripsi.absensiwifi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeAct extends AppCompatActivity {
    ImageView btn_absen;
    LinearLayout btn_history, btn_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btn_absen = findViewById(R.id.btn_absen);
        btn_history = findViewById(R.id.btn_history);
        btn_profile = findViewById(R.id.btn_profile);
        btn_absen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoabsen = new Intent(HomeAct.this, AbsenAct.class);
                startActivity(gotoabsen);
                finish();
            }
        });

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotohistory = new Intent(HomeAct.this, HistoryAct.class);
                startActivity(gotohistory);
                finish();
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeAct.this, ProfileAct.class);
                startActivity(gotoprofile);
                finish();
            }
        });

//        try {
//            JSONObject ProfileObject = new JSONObject(new Gson().toJson(response.body().getData()));
//            st_nama = ProfileObject.getString("nama");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        tvNama.setText(st_nama);
    }
}
