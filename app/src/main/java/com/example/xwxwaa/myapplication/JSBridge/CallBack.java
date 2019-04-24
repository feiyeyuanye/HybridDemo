package com.example.xwxwaa.myapplication.JSBridge;

import android.webkit.WebView;

import org.json.JSONObject;

/**
 * 回调
 * Created by xwxwaa on 2018/7/27.
 */

public class CallBack {

    public String mPort;
    public WebView mWebView;

    /**
     * 将java代码执行后的结果，告诉js层，让js执行自己的回调函数。
     * @param webView WebView
     * @param port 回调函数在js层中存储的地址
     */
    public CallBack(WebView webView, String port){
        this.mWebView= webView;
        this.mPort=port;
    }

    /**
     * java层执行这个回调函数，通知js层执行自己的回调函数。
     * @param jsonObject JSONObject
     */
    public void apply(JSONObject jsonObject){
        if (mWebView!=null){
            //               js层的代码  定义好的回调方法     地址         值
            mWebView.loadUrl("javascript:onAndroidFinished('"+mPort+"',"+ String.valueOf(jsonObject)+")");
        }
    }
}
