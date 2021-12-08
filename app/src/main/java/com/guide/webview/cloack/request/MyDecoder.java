
package com.guide.webview.cloack.request;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

/**
 * Дешифруем url RemoteConfig через base64 (можно использовать другие)
 * Например: у нас есть https://rentry.co/3u64p/ - это baseUrl
 * в зашифрованном виде - aHR0cHM6Ly9yZW50cnkuY28vM3U2NHAv
 * шифруем онлайн через: http://crypt-online.ru/crypts/base64/
 * дешифруем внутри приложения
 */
public class MyDecoder {
    private String url;

    public MyDecoder(String url) {
        this.url = url;
    }

    public String decode() {
        return new String(Base64.decode(url, Base64.DEFAULT), StandardCharsets.UTF_8);
    }
}
