package com.guide.webview.webview.partner;

import static com.guide.webview.zaglushka.gameutils.Constants.DEP;
import static com.guide.webview.zaglushka.gameutils.Constants.HASH_NAME;
import static com.guide.webview.zaglushka.gameutils.Constants.USER_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Base64;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appsflyer.AppsFlyerLib;
import com.guide.webview.R;
import com.guide.webview.webview.webviewsettings.MyWebViewChromeClient;
import com.guide.webview.webview.webviewsettings.MyWebViewClient;
import com.guide.webview.webview.webviewsettings.MyWebViewSettings;
import com.guide.webview.zaglushka.gameutils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Класс отвечает за открытие webView, сохранение финальной ссылки, показ страницы партнера
 */
public class PartnersActivity extends Activity {
    private final static int FILECHOOSER_RESULTCODE = 1;
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private WebView webView;
    private ValueCallback<Uri> mUploadMessage;
    private SharedPreferences sharedPreferences;
    public ValueCallback<Uri[]> mFilePathCallback;
    private String filePath;
    private Uri mCapturedImageURI = null;
    private String dataload;
    private Timer timer;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partners);
        webView = findViewById(R.id.my_webview);
        // инитим настройки и editor для сохранения ссылки
        sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        new MyWebViewSettings(webView);// задаем настройки для webView

        webView.setWebViewClient(new MyWebViewClient());// задаем webViewClient

        webView.setWebChromeClient(new MyWebViewChromeClient(PartnersActivity.this));// задаем webViewChromeClient
        // проверяем если ли в настройках сохраненая сформированная ссылка
        if (!sharedPreferences.contains(USER_URL)) {// если нет, то сохраняем
            editor.putString(USER_URL, getIntent().getStringExtra("urlCampaign"));
            editor.apply();
        }// если есть, то ничего не делаем

        webView.loadUrl(sharedPreferences.getString(USER_URL, ""));// загружаем сформированную ссылку
        Log.d("url", webView.getUrl());

        checkDep();// проверяем действия пользователя

    }

    /**
     * Проверка действия пользователя
     */
    public void checkDep() {
        if (isFirstDep()) {// если пользователь сделал первый депозит, то этот метод больше не выполняется
            timer = new Timer();// каждую минуту отправляем запрост на сервер
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (isFirstDep()) {
                        try {
                            //отправка запроса
                            OkHttpClient okHttpClient = new OkHttpClient();
                            String userId = sharedPreferences.getString(HASH_NAME, "");// hash пользователя
                            HttpUrl.Builder urlBuilder = HttpUrl.parse(new String(Base64.decode("aHR0cDovL2ZydXR0aWtpLmNvbS9wb3N0YmFjay9nZXQ=", Base64.DEFAULT), StandardCharsets.UTF_8)).newBuilder();// url сервера
                            urlBuilder.addQueryParameter("hash", userId);// к url сервера добавляем hash пользователя
                            urlBuilder.addQueryParameter("app_id", "com.guide.webview");  // к url+hash добавляем пакет приложения
                            String finalUrl = urlBuilder.build().toString();
                            okHttpClient = new OkHttpClient.Builder()
                                    .connectTimeout(60, TimeUnit.SECONDS)
                                    .writeTimeout(60, TimeUnit.SECONDS)
                                    .readTimeout(60, TimeUnit.SECONDS)
                                    .build();
                            Request request = new Request.Builder()
                                    .url(finalUrl)
                                    .build();
                            // Получаем ответ
                            Response response = okHttpClient.newCall(request).execute();
                            String answer = response.body().string();
                            if (answer.equals("[]")) {// ответ может быть пустым и закрашить прилку
                                return;
                            } else { // если ответ не пустой, парсим JSON
                                JSONObject dataResponse = new JSONObject(answer);
                                boolean isFull = dataResponse.length() > 0;
                                if (isFull) {// пользователь выполнил какое-то действие
                                    // отправляем event в appsFlyer
                                    Map<String, Object> eventValues = new HashMap<>();
                                    eventValues.put("reg", dataResponse.getString("is_registration"));
                                    eventValues.put("dep", dataResponse.getString("is_deposit"));
                                    // выполняем отправку событий пока пользователь не сделает депозит
                                    AppsFlyerLib.getInstance().logEvent(getApplicationContext(), "first_deposit", eventValues);// отправляем все данные из сервера
                                    if (dataResponse.getString("is_deposit").equals("true")) {// если был сделан депозит отправляем эти данные и больше не выполняем этот метод
                                        AppsFlyerLib.getInstance().logEvent(getApplicationContext(), "first_deposit", eventValues);
                                        saveEvent(DEP, "send");
                                    }
                                }
                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }, 0, 60000);

        }
    }

    /**
     * Сохраняем данные о депозите
     * @param name
     * @param param
     */
    private void saveEvent(String name, String param) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, param);
        editor.apply();

    }

    /**
     * Проверяем был ли сделан депозит
     * @return
     */
    public boolean isFirstDep() {
        return !sharedPreferences.contains(DEP);
    }

    /**
     * Возможность добавления файлов в webView
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        } else {
        }
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                if (filePath != null) {
                    results = new Uri[]{Uri.parse(filePath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        Uri result = null;
        try {
            if (resultCode != RESULT_OK) {
                result = null;
            } else {
                result = data == null ? mCapturedImageURI : data.getData();
            }
        } catch (Exception e) {
        }
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }

    /**
     * Сохранения состояния webView
     * @param savedInstanceState
     * @param persistentState
     */
    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        webView.restoreState(savedInstanceState);
    }

    /**
     * Сохранения состояния webView
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        webView.saveState(outState);
    }

    /**
     * если активити закрылось останавливаем таймер
     */
    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }

    /**
     * Возвожность перехода между страничками webView
     */
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {// если были переходы между страницами webview, разрешаем вернуться обратно
            webView.goBack();
        } else {// если это последняя страница закрываем приложение и выключаем таймер
            timer.cancel();
            finish();
        }
    }
}
