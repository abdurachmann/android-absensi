package com.skripsi.absensiwifi;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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

public class PinAct extends AppCompatActivity {

    private static final String TAG = PinAct.class.getSimpleName();
    private DataService service;

    private EditText etPin;
    Button btnVerify;
    String nik, pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        service = ServiceGenerator.createBaseService(this, DataService.class);
        initListener();
    }

    @SuppressLint("WrongViewCast")
    private void initListener() {
        etPin = findViewById(R.id.edt_pin);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE);
        nik = pref.getString("nik", "");

        System.out.print("NIK : " + nik);

        getPin(nik);

        btnVerify = findViewById(R.id.btn_verify_pin);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pinInput = etPin.getText().toString();

                if(isEmpty(pinInput))
                    etPin.setError("PIN tidak boleh kosong");
                else
                    verifyPin(nik, pinInput);
            }
        });
    }

    @SuppressLint("HardwareIds")
    private void verifyPin(final String nik, final String pin) {
        Call<BaseResponse> call = service.apiVerifyPin(nik, pin);
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
                            JSONObject DataPegawaiObject = new JSONObject(new Gson().toJson(response.body().getData()));

                            // Data Response
                            String pin = DataPegawaiObject.getString("pin");
                            String pinInput = etPin.getText().toString();

                            if (pin.equals(pinInput)) {
                                Toast.makeText(PinAct.this,"Sinkron berhasil.", Toast.LENGTH_SHORT).show();

                                // Switch Activity
                                Intent AbsenActivity = new Intent(PinAct.this, AbsenAct.class);
                                startActivity(AbsenActivity);
                            }else{
                                Toast.makeText(PinAct.this, "Pin Tidak Valid!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(PinAct.this, "Pin Tidak Valid!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void getPin(String nik) {
        Call<BaseResponse> call = service.apiProfile(nik);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {
                    try {
                        JSONObject ProfileObject = new JSONObject(new Gson().toJson(response.body().getData()));
                        pin = ProfileObject.getString("pin");

                        showNotif(pin);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    private void showNotif(String pin) {
        System.out.print("Show Notify");

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("PIN Sync") // title for notification
                .setContentText("Berikut PIN Sync Anda : " + pin)// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), PinAct.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
