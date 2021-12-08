package com.guide.webview.cloack.enterspot;

import static com.guide.webview.zaglushka.gameutils.Constants.HASH_NAME;
import static com.guide.webview.zaglushka.gameutils.Constants.PREF_NAME;
import static com.guide.webview.zaglushka.gameutils.Constants.USER_URL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.guide.webview.R;
import com.guide.webview.cloack.appsflyer.AppsFlyerCheck;
import com.guide.webview.cloack.request.RemoutConfigCallBack;
import com.guide.webview.cloack.request.RemoutConfigRequest;
import com.guide.webview.webview.partner.PartnersActivity;
import com.guide.webview.zaglushka.activities.GameStartMenuActivity;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.Random;

/**
 * Стартовая точка приложения.
 * Класс отвечает за: получение параметров из RemoteConfig, создание и сохранение Hash-значения пользователя,
 * Быстрое открытие веб страницы партнера.
 */
public class AppLoadActivity extends Activity implements RemoutConfigCallBack {
    private SharedPreferences sharedPreferences;
    private String hash;
    public ProgressBar progressBar;
    public Sprite doubleBounce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_load);

        // инитим настройки приложения (в них сохраняем сформированую ссылку пользователя и его Hash)
        // инитим customProgressbar
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        progressBar = findViewById(R.id.spin_kit);
        doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        // выполним проверку заходил ли пользователь уже в приложение.
        // если он заходил, то у него есть сформированная ссылка и его уникальный Hash
        checkFirstEnter();
    }

    /**
     * Проверка №1
     */
    public void checkFirstEnter() {
        // из настроек приложения пытаемся получить сформированую ссылку пользователя
        //если она есть сразу открываем WebView с этой ссылкой и не выполняем больше никаких действий
        //это необходимо для ускорения работы приложения, если приложение будет медленно работать
        //трафик пользователей будет падать, что не есть хорошо(
        if (sharedPreferences.contains(USER_URL)) {
            Intent intent = new Intent(this, PartnersActivity.class);
            startActivity(intent);
            finish();
        } else {
            // если пользователь зашел в приложение впервые получаем данные из RemoteConfig
            new RemoutConfigRequest(this);
        }
    }

    /**
     * Класс имплементирует методы callBack, тут мы получаем ответ из RemoteConfig
     * @param firstSwitch - отвечает за показ заглушки или выполнения дальнейших действий
     * @param secondSwitch - отвечает за органическую и неорганическую установки
     * @param domen - ссылка партнера
     *
     */
    @Override
    public void getted(String firstSwitch, String secondSwitch, String domen) {
        // если firstSwitch равен true, то создаем hash пользователя, сохраняем его и добавляем к ссылке(domen)
        // далее инитим AppsFlyer
        if (firstSwitch.equals("true")) {
            hash = randomString(30);// создание hash пользователя
            String mainUrl = domen.concat("?hash=").concat(hash);// добавления hash к domen
            saveHash(HASH_NAME, hash);// сохранение hash пользователя
            new AppsFlyerCheck(this, getApplicationContext(), secondSwitch, mainUrl);// инитим appsFlyer
        } else {
            // если firstSwitch равен false, открываем заглушку без каких либо действий
            startActivity(new Intent(getApplicationContext(), GameStartMenuActivity.class));
            finish();
        }

    }

    @Override
    public void isFail() {

    }

    /**
     * Сохраняем hash  настройки
     * @param name
     * @param param
     */
    private void saveHash(String name, String param) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, param);
        editor.apply();
    }

    /**
     * Создаем hash пользователя
     * Этот метод никогда не меняем!
     * @param len
     * @return
     */
    private String randomString(int len) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                    .charAt(random.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                            .length())));
        }
        return sb.toString();
    }
}

