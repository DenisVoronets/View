package com.guide.webview.webview.webviewsettings;

import android.content.Intent;
import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.guide.webview.webview.partner.PartnersActivity;

/**
 * Этот класс необходим для взаимодействия пользователя со страничкой загруденной в webView
 * добавления файлов, переход по ссылкам и т.д.
 * этот класс не меняем (только перевызываем методы, добавляем конструкторы, доп. настройки и задаем им значения false)
 * уникализируйте этот класс, но так чтобы ничего не сломалось
 */
public class MyWebViewChromeClient extends WebChromeClient {
    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private PartnersActivity webViewActivity;

    public MyWebViewChromeClient(PartnersActivity webViewActivity) {
        this.webViewActivity = webViewActivity;
    }

    public boolean onShowFileChooser(WebView view,
                                     ValueCallback<Uri[]> filePath,
                                     FileChooserParams fileChooserParams) {
        if (webViewActivity.mFilePathCallback != null) {
            webViewActivity.mFilePathCallback.onReceiveValue(null);
        }
        webViewActivity.mFilePathCallback = filePath;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent[] intentArray = new Intent[0];
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select Option:");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        webViewActivity.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
        return true;
    }
}
