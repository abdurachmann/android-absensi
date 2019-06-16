package com.skripsi.absensiwifi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataProfile implements Parcelable {
    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggallahir() {
        return tanggallahir;
    }

    public void setTanggallahir(String tanggallahir) {
        this.tanggallahir = tanggallahir;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    @SerializedName("nik")
    String nik;
    @SerializedName("nama")
    String nama;
    @SerializedName("tanggallahir")
    String tanggallahir;
    @SerializedName("alamat")
    String alamat;

    protected DataProfile(Parcel in) {
        nik = in.readString();
        nama = in.readString();
        tanggallahir = in.readString();
        alamat = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nik);
        dest.writeString(nama);
        dest.writeString(tanggallahir);
        dest.writeString(alamat);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataProfile> CREATOR = new Creator<DataProfile>() {
        @Override
        public DataProfile createFromParcel(Parcel in) {
            return new DataProfile(in);
        }

        @Override
        public DataProfile[] newArray(int size) {
            return new DataProfile[size];
        }
    };
}