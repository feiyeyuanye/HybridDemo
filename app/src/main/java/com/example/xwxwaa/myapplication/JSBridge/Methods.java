package com.example.xwxwaa.myapplication.JSBridge;

import android.webkit.WebView;

import org.json.JSONObject;

/**
 * 这个类中包含所有暴露给js的方法。
 * Created by xwxwaa on 2018/7/27.
 */

public class Methods {
    /**
     * 暴露给js的一个方法
     * @param v
     * @param param
     * @param callBack
     */
    public static void ShowToast(WebView v, JSONObject param, CallBack callBack){
        // 表示js层成功将消息传递给java层，同时java层执行了此方法，
        // 并且将执行结果告诉js层，让js层执行自己的回调函数。

        // 将参数传化为一个字符串
        String message=param.optString("msg");
        // Toast.makeText(v.getContext(),message,Toast.LENGTH_SHORT).show();
        if (callBack!=null){
            // 如果回调函数不为空，则调用js层的回调函数。
            if ("0".equals(message)){
                // 根据msg信息做区分，执行相应的回调函数。
                onWebViewDataListener.onWebViewDataListener(callBack);
             }
        }
    }

    /**
     * 回调
     */
    public static OnWebViewDataListener onWebViewDataListener;
    public interface OnWebViewDataListener {
        void onWebViewDataListener(CallBack callBack);
    }
    public void setOnWebViewDataListener(OnWebViewDataListener onWebViewDataListener) {
        this.onWebViewDataListener = onWebViewDataListener;
    }
}
