package com.example.xwxwaa.myapplication.JSBridge;

import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 自定义的ChromeClient
 * Created by xwxwaa on 2018/7/27.
 */

public class JSBridgeChromeClient extends WebChromeClient {
    /**
     * 截取js层传上来的url消息，执行相应的java方法。
     * @param view
     * @param url
     * @param message
     * @param defaultValue
     * @param result
     * @return
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        // 执行自己的方法
        result.confirm(JSBridge.callJava(view,message));
        return true;
    }
}
