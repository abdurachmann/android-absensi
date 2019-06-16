package com.skripsi.absensiwifi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataHistory implements Parcelable {
    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAbsenmasuk() {
        return absenmasuk;
    }

    public void setAbsenmasuk(String absenmasuk) {
        this.absenmasuk = absenmasuk;
    }

    public String getAbsenkeluar() {
        return absenkeluar;
    }

    public void setAbsenkeluar(String absenkeluar) {
        this.absenkeluar = absenkeluar;
    }

    @SerializedName("tanggal")
    String tanggal;
    @SerializedName("absenmasuk")
    String absenmasuk;
    @SerializedName("absenkeluar")
    String absenkeluar;

    protected DataHistory(Parcel in) {
        tanggal = in.readString();
        absenmasuk = in.readString();
        absenkeluar = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tanggal);
        dest.writeString(absenmasuk);
        dest.writeString(absenkeluar);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataHistory> CREATOR = new Creator<DataHistory>() {
        @Override
        public DataHistory createFromParcel(Parcel in) {
            return new DataHistory(in);
        }

        @Override
        public DataHistory[] newArray(int size) {
            return new DataHistory[size];
        }
    };
}