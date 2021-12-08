package com.guide.webview.webview.webviewsettings;

import android.graphics.Bitmap;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Этот класс необходим для отслеживания состояния страницы
 * * этот класс не меняем (только перевызываем методы, добавляем конструкторы, доп. настройки и задаем им значения false)
 *  * уникализируйте этот класс, но так чтобы ничего не сломалось
 */
public class MyWebViewClient extends WebViewClient {

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        // progressbar visible;
    }
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        view.loadUrl(request.getUrl().toString());
        // track, pars, control redirects urls
        return true;
    }
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        // progressbar invisible;
    }
}
