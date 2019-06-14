package com.skripsi.absensiwifi.network.service;

import com.skripsi.absensiwifi.Endpoint;
import com.skripsi.absensiwifi.model.DataHistory;
import com.skripsi.absensiwifi.network.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataService {
    @FormUrlEncoded
    @POST(Endpoint.API_LOGIN)
    Call<BaseResponse> apiLogin(
            @Field("nik") String nik,
            @Field("tanggallahir") String tanggallahir
    );

    @GET(Endpoint.API_PROFILE)
    Call<BaseResponse> apiProfile(
            @Query("nik") String nik
    );

    @FormUrlEncoded
    @POST(Endpoint.API_ABSEN)
    Call<BaseResponse> apiAbsen(
            @Field("nik") String nik,
            @Field("jenisabsen") String jenisabsen,
            @Field("jamabsen") String jamabsen,
            @Field("macaddress") String macaddress,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @GET(Endpoint.API_HISTORY)
    Call<BaseResponse<List<DataHistory>>> apiHistory(
            @Query("nik") String nik
    );

    @FormUrlEncoded
    @POST(Endpoint.API_SYNCGPS)
    Call<BaseResponse> apiSyncGps(
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );
}