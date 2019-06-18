package com.skripsi.absensiwifi;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.skripsi.absensiwifi.model.DataHistory;
import com.skripsi.absensiwifi.network.ServiceGenerator;
import com.skripsi.absensiwifi.network.response.BaseResponse;
import com.skripsi.absensiwifi.network.service.DataService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;


public class AbsenAct extends AppCompatActivity {
    ImageView btn_back;

    private static final String TAG = LoginAct.class.getSimpleName();

    private DataService service = ServiceGenerator.createBaseService(this, DataService.class);

    // office states
    private double officeLatitude = 0.0d;
    private double officeLongitude = 0.0d;
    private float acceptedRadius = 50.0f; // maximum radius in meters from office coordinate

    // device states
    private String macAddress = "";
    private Location currentLocation = null;

    // absen state
    private boolean isPermitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        // get device's mac address
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//
//        macAddress = wifiInfo.getMacAddress();
//
//        System.out.println("Mac Address: " + macAddress);
//
//        LocationListener locationListener = (LocationListener) new AbsensiLocationListener();
//
//        // get current location coordinates
//        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2500, 0, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2500, 0, locationListener);

        // get office data
//        getOfficeData();

        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoprev = new Intent(AbsenAct.this, HomeAct.class);
                startActivity(backtoprev);
                finish();
            }
        });

        LinearLayout btn_sync = findViewById(R.id.btn_sync);
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOfficeData();
            }
        });

        LinearLayout btn_masuk = findViewById(R.id.btn_masuk);
        btn_masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absen(true);
            }
        });

        LinearLayout btn_keluar = findViewById(R.id.btn_keluar);
        btn_keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absen(false);
            }
        });
    }

    private void absen(boolean isMasuk) {
        if (!isPermitted) {
            AlertDialog alertDialog = new AlertDialog.Builder(AbsenAct.this).create();
            alertDialog.setTitle("Absen Gagal");
            alertDialog.setMessage("Anda tidak dapat melakukan absen, lokasi atau alamat MAC tidak sesuai.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("USER_ACCESS", Context.MODE_PRIVATE); // 0 - for private mode

        String nik = pref.getString("nik", "");
//        String latitude = String.valueOf(currentLocation.getLatitude());
//        String longitude = String.valueOf(currentLocation.getLongitude());

        String latitude = "57.34938493";
        String longitude = "-1028.238748973";

        Call<BaseResponse> call = service.apiAbsen(nik, macAddress, latitude, longitude, isMasuk);

        call.enqueue(new Callback<BaseResponse>() {

            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.code() == 200) {

                    BaseResponse AbsenObject = response.body();
                    String returnedResponse = AbsenObject.status;

                    if(returnedResponse.trim().equals("true")) {
                        Toast.makeText(AbsenAct.this, "Berhasil Absen", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AbsenAct.this, "Gagal Absen", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Log.e(TAG + ".error", t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private class AbsensiLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location _currentLocation) {
            currentLocation = _currentLocation;

            validateStates();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            System.out.printf("GPS status: %d.\n", status);
        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.printf("Your GPS is enabled.\n");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.printf("Your GPS is disabled.\n");
        }
    }

    private void getOfficeData() {
        Call<ResponseBody> call = service.apiOffice();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    JSONObject bd = null;
                    try {
                        bd = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    try {
                        officeLatitude = bd.getDouble("latitude");
                        officeLongitude = bd.getDouble("longitude");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }

                    System.out.printf("Latitude\t:%f\nLongitude\t:%f\n", officeLatitude, officeLongitude);

                    // validate user location and mac address
                    validateStates();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(AbsenAct.class.getSimpleName() + ".error", t.toString());

                AlertDialog alertDialog = new AlertDialog.Builder(AbsenAct.this).create();
                alertDialog.setTitle("Sinkron Gagal");
                alertDialog.setMessage("Gagal terhubung dengan jaringan sistem absensi.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void validateStates() {
        if (currentLocation == null)
            return;

        Location companyLocation = new Location("");
        companyLocation.setLatitude(officeLatitude);
        companyLocation.setLongitude(officeLongitude);

        System.out.printf(
                "Office location: %s\t%s\n" +
                "Your location: %s\t%s\n",
                String.valueOf(companyLocation.getLatitude()),
                String.valueOf(companyLocation.getLongitude()),
                String.valueOf(currentLocation.getLatitude()),
                String.valueOf(currentLocation.getLongitude())
        );

        float distance = companyLocation.distanceTo(currentLocation);

        LinearLayout btn_masuk = findViewById(R.id.btn_masuk);
        LinearLayout btn_keluar = findViewById(R.id.btn_keluar);

        ImageView btn_absen_masuk = findViewById(R.id.btn_absen_masuk);
        ImageView btn_absen_keluar = findViewById(R.id.btn_absen_keluar);

        TextView txt_btn_masuk = findViewById(R.id.txt_btn_masuk);
        TextView txt_btn_keluar = findViewById(R.id.txt_btn_keluar);

        isPermitted = false;

        if (distance > acceptedRadius) {
            System.out.println("Your distance from given office coordinate is: "+ distance +" m");

            btn_masuk.setClickable(false);
            btn_keluar.setClickable(false);

            btn_masuk.getBackground().setAlpha(200);
            btn_keluar.getBackground().setAlpha(200);
            btn_absen_masuk.setAlpha(0.4f);
            btn_absen_keluar.setAlpha(0.4f);
            txt_btn_masuk.setAlpha(0.6f);
            txt_btn_keluar.setAlpha(0.6f);

            return;
        }

        isPermitted = true;

        btn_masuk.setClickable(true);
        btn_keluar.setClickable(true);

        btn_masuk.getBackground().setAlpha(255);
        btn_keluar.getBackground().setAlpha(255);
        btn_absen_masuk.setAlpha(1.0f);
        btn_absen_keluar.setAlpha(1.0f);
        txt_btn_masuk.setAlpha(1.0f);
        txt_btn_keluar.setAlpha(1.0f);
    }
}
