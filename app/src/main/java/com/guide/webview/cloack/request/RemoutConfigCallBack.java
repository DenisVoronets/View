package com.guide.webview.cloack.request;

/**
 * CallBack для передачи параметров из RemoteConfig
 */
public interface RemoutConfigCallBack {
     void getted(String firstSwitch, String secondSwitch,String domen);// данные получили
     void isFail();// данные не получили (необязательно)
}
