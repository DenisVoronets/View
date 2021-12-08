package com.guide.webview.webview.webviewsettings;

import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Настройки webView
 * этот класс не меняем (только перевызываем методы, добавляем конструкторы, доп. настройки и задаем им значения false)
 * уникализируйте этот класс, но так чтобы ничего не сломалось
 */
public class MyWebViewSettings {
    public MyWebViewSettings(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusableInTouchMode(true);
        webView.getSettings().setMixedContentMode(0);
        CookieManager.getInstance().setAcceptCookie(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setSaveFormData(true);
        webView.setSaveEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setFocusable(true);

    }
}
