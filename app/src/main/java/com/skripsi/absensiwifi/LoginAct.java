package com.skripsi.absensiwifi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.text.TextUtils.isEmpty;

public class LoginAct extends AppCompatActivity {

    private static final String TAG = LoginAct.class.getSimpleName();
    private DataService service;

    private EditText etNik;
    private EditText etPassword;

    Button btnLogin;
    TextView lupaPassword;

    // Device States
    private String macAddressDevice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        lupaPassword = findViewById(R.id.lupa_pass);
        lupaPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotolupa = new Intent(LoginAct.this, LupaAct.class);
                startActivity(gotolupa);
                finish();
            }
        });

        initListener();
        service = ServiceGenerator.createBaseService(this, DataService.class);
    }

    @SuppressLint("WrongViewCast")
    private void initListener() {
        btnLogin = findViewById(R.id.btn_login);
        etNik = findViewById(R.id.edt_nik);
        etPassword = findViewById(R.id.edt_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
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

    @SuppressLint("HardwareIds")
    private void Login(final String nik, String password) {
        Call<BaseResponse> call = service.apiLogin(nik, password);

        // get device's mac address
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        macAddressDevice = getMacAddr();
        System.out.println("Mac Address: " + macAddressDevice);

        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {

                    BaseResponse LoginObject = response.body();
                    String returnedResponse = LoginObject.status;

                    if(returnedResponse.trim().equals("true")) {

                        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        try {
                            JSONObject DataLoginObject = new JSONObject(new Gson().toJson(response.body().getData()));

                            // Data Response
                            String nik = DataLoginObject.getString("nik");
                            String nama = DataLoginObject.getString("nama");
                            String macaddressRegistered = DataLoginObject.getString("macaddress");

//                            if (macAddressDevice.equals(macaddressRegistered)) {
                                // SharedPreferences Value
                                Toast.makeText(LoginAct.this, "Berhasil", Toast.LENGTH_SHORT).show();
                                editor.putString("nik", nik);
                                editor.putString("nama", nama);
                                editor.apply();

                                // Switch Activity
                                Intent HomeActivity = new Intent(LoginAct.this, HomeAct.class);
                                startActivity(HomeActivity);
//                            }else{
//                                Toast.makeText(LoginAct.this, "Gunakan Handphone Anda!", Toast.LENGTH_SHORT).show();
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }
}
