package com.skripsi.absensiwifi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.service.DataService;

import static android.text.TextUtils.isEmpty;

public class UbahAct extends AppCompatActivity {
    ImageView btn_back;
    Button btn_kirim;
    private static final String TAG = UbahAct.class.getSimpleName();
    private DataService service;
    private EditText pass_lama;
    private EditText pass_baru;
    private  EditText confirm_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah);

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(UbahAct.this, ProfileAct.class);
                startActivity(gotoprofile);
                finish();
            }
        });

        initListener();
        service = ServiceGenerator.createBaseService(this, DataService.class);
    }

    private void initListener() {
        pass_lama = findViewById(R.id.pass_lama);
        pass_baru = findViewById(R.id.pass_baru);
        confirm_pass = findViewById(R.id.confrim_pass);
        btn_kirim = findViewById(R.id.btn_kirim);
        btn_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passwordlama = pass_lama.getText().toString();
                String passwordbaru = pass_baru.getText().toString();
                String confirmpassword = confirm_pass.getText().toString();

                if (isEmpty(passwordlama))
                    pass_lama.setError("Password tidak boleh kosong");
                else Kirim(passwordlama, passwordbaru, confirmpassword);

                if (isEmpty(passwordbaru))
                    pass_baru.setError("Masukkan password anda");
                else Kirim(passwordlama, passwordbaru, confirmpassword);

                if (isEmpty(confirmpassword))
                    confirm_pass.setError("Masukkan password anda");
                else Kirim(passwordlama, passwordbaru, confirmpassword);
            }
        });
    }

    private void Kirim(String passwordlama, String passwordbaru, String confirmpassword) {
    }
}
