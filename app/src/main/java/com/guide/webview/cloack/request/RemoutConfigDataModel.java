package com.guide.webview.cloack.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Класс имитирует вид данных, лежащих на RemoteConfig
 */
public class RemoutConfigDataModel {

    @SerializedName("firstSwitch")
    @Expose
    private String firstSwitch;

    @SerializedName("secondSwitch")
    @Expose
    private String secondSwitch;
    @SerializedName("domen")
    @Expose
    private String domen;

    public String getFirstSwitch() {
        return firstSwitch;
    }

    public String getSecondSwitch() {
        return secondSwitch;
    }

    public String getDomen() {
        return domen;
    }
}
