package com.skripsi.absensiwifi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;


public class AbsenAct extends AppCompatActivity {
    ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absen);

        // get device's mac address
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        String macAddress = wifiInfo.getMacAddress();

        System.out.println("Mac Address: " + macAddress);

        LocationListener locationListener = (LocationListener) new AbsensiLocationListener();

        // get current location coordinates
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoprev = new Intent(AbsenAct.this,HomeAct.class);
                startActivity(backtoprev);
                finish();
            }
        });
    }

    private class AbsensiLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location currentLocation) {
            System.out.printf("Your location: %f\t%f\n", currentLocation.getLatitude(), currentLocation.getLongitude());

            Location companyLocation = new Location("");
            companyLocation.setLatitude(-6.932276371608801);
            companyLocation.setLongitude(107.57246031481941);

            float acceptedRadius = 50.0f;

            float distance = companyLocation.distanceTo(currentLocation);
            if (distance > acceptedRadius) {
                System.out.println("Your distance from given office coordinate is: "+ distance +" m");
                // TODO: disabled absen btn and show location information message.
                return;
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
