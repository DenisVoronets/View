package com.guide.webview.cloack.request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Создание клиента Retrofit + serverUrl
 */
public class RetrofitClient {
    private String serverUrl;

    public RetrofitClient(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    public Retrofit createRequest(){
        return new Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}

