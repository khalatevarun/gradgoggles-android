package com.example.android.a2020.api.service;

import com.example.android.a2020.BuildConfig;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitBuilder {

    private static final String BASE_URL = "https://ggapi.rajrajhans.com/";

    private final static OkHttpClient client = buildClient();
    private static Retrofit retrofit = builderRetrofit(client);

    private static OkHttpClient buildClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        Request.Builder builder = request.newBuilder()
                                .addHeader("Content-Type", "multipart/form-data; boundary=<calculated when request is sent>")
                                .addHeader("Accept", "*/*")
                                .addHeader("Accept-Encoding", "gzip, deflate, br")
                                .addHeader("Connection", "keep-alive");

                        request = builder.build();

                        return chain.proceed(request);


                    }
                });
        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();

    }

    private static Retrofit builderRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();


    }

    public static <T> T createService(Class<T> service){
        return retrofit.create(service);
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }


}







