/*-- package com.skripsi.absensiwifi.network;

import android.content.Context;

import com.skripsi.absensiwifi.BuildConfig;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static OkHttpClient.Builder builder() {
        OkHttpClient.Builder okhttpBuilder = new OkHttpClient().newBuilder();
        okhttpBuilder.connectTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.writeTimeout(60, TimeUnit.SECONDS);
        okhttpBuilder.readTimeout(60, TimeUnit.SECONDS);

        return okhttpBuilder;
    }

    private static HttpLoggingInterceptor interceptor(){
        HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return  interceptor;
    }

    public static  <S> S createBaseService(Context context, Class <S> serviceClass) {
        OkHttpClient.Builder okhttpBuilder = builder();
        if (BuildConfig.DEBUG){
            okhttpBuilder.addInterceptor(new Interceptor());
        }

        okhttpBuilder.addInterceptor(new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException{
                Request request = chain.request();
                Request newReq = request.newBuilder()
                        .header("Accept", "application/json")
                        .build();
                return chain.proceed(newReq);
            }
        });

        okHttpClient client = okhttpBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Endpoint.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }
} --*/
