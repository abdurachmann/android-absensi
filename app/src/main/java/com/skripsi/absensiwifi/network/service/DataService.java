package com.skripsi.absensiwifi.network.service;

import com.google.gson.JsonElement;
import com.skripsi.absensiwifi.network.Endpoint;
import com.skripsi.absensiwifi.model.DataHistory;
import com.skripsi.absensiwifi.network.response.BaseResponse;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
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
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST(Endpoint.API_RESET)
    Call<BaseResponse> apiResetPassword(
            @Field("nik") String nik
    );

    @FormUrlEncoded
    @POST(Endpoint.API_CHANGE)
    Call<BaseResponse> apiChangePassword(
            @Field("nik") String nik,
            @Field("passwordlama") String passwordlama,
            @Field("passwordbaru") String passwordbaru
    );

    @GET(Endpoint.API_PROFILE)
    Call<BaseResponse> apiProfile(
            @Query("nik") String nik
    );

    @FormUrlEncoded
    @POST(Endpoint.API_ABSEN)
    Call<BaseResponse> apiAbsen(
            @Field("nik") String nik,
            @Field("macaddress") String macaddress,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("jenisabsen") Boolean jenisabsen
    );

    @GET(Endpoint.API_HISTORY)
    Call<BaseResponse<List<DataHistory>>> apiHistory(
            @Query("nik") String nik,
            @Query("bulan") String bulan,
            @Query("tahun") String tahun
    );

    @FormUrlEncoded
    @POST(Endpoint.API_SYNCGPS)
    Call<BaseResponse> apiSyncGps(
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @GET(Endpoint.API_VERIFY)
    Call<BaseResponse> apiVerifyPin(
            @Query("nik") String nik,
            @Query("pin") String pin
    );

    @GET(value = Endpoint.API_ABSEN + "/office")
    Call<ResponseBody> apiOffice();
}