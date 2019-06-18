package com.skripsi.absensiwifi;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setNik(String nik) {
        prefs.edit().putString("nik", nik).commit();
    }

    public String getNik() {
        String nik = prefs.getString("nik","");
        return nik;
    }

    public void setNama(String nama) {
        prefs.edit().putString("nama", nama).commit();
    }

    public String getNama() {
        String nama = prefs.getString("nama","");
        return nama;
    }
}
