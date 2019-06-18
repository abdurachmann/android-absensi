package com.skripsi.absensiwifi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeAct extends AppCompatActivity {
    ImageView btn_absen;
    LinearLayout btn_history, btn_profile;
    TextView txtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        txtName = findViewById(R.id.txtName);
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

<<<<<<< HEAD
//        try {
//            JSONObject ProfileObject = new JSONObject(new Gson().toJson(response.body().getData()));
//            st_nama = ProfileObject.getString("nama");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        tvNama.setText(st_nama);
=======
        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode
        String user_name = pref.getString("nama", "");

        if (user_name.isEmpty()) {
            // merubah activity ke activity lain
            Intent gotologin = new Intent(HomeAct.this, LoginAct.class);
            startActivity(gotologin);
            finish();
        } else {
            txtName.setText(user_name);
        }
>>>>>>> 99e8772d36b30d86073730a73852a0ad5a8ae958
    }
}
