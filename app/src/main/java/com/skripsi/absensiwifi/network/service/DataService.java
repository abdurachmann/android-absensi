/*-- package com.skripsi.absensiwifi.network.service;

import com.skripsi.absensiwifi.model.Data;
import com.skripsi.absensiwifi.network.Endpoint;
import com.skripsi.absensiwifi.network.response.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataService {
    @FormUrlEncoded
    @POST(Endpoint.API_CREATE)
    Call<BaseResponse> apiCreate(@Field("name")String nama);

    @GET(Endpoint.API_READ)
    Call<BaseResponse<List<Data>>> apiRead();

    @FormUrlEncoded
    @POST(Endpoint.API_UPDATE+ "{id}")
    Call<BaseResponse> apiUpdate(
            @Path("id") String id,
            @Field("name") String name
    );

    @POST(Endpoint.API_DELETE+"{id}")
    Call<BaseResponse> apiDelete(@Path("id") String id);
}
--*/
