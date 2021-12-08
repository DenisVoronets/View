package com.guide.webview.cloack.request;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * GET-запрос
 */
public interface RemoutConfugAPI {
    @GET("raw")// указываем последний параметр в ссылке (https://rentry.co/3u64p/raw)
    Call<RemoutConfigDataModel> doCall();
}
