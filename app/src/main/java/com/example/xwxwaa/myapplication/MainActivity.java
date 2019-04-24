package com.example.xwxwaa.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.xwxwaa.myapplication.JSBridge.CallBack;
import com.example.xwxwaa.myapplication.JSBridge.JSBridge;
import com.example.xwxwaa.myapplication.JSBridge.JSBridgeChromeClient;
import com.example.xwxwaa.myapplication.JSBridge.Methods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Hybrid
 */
public class MainActivity extends AppCompatActivity implements Methods.OnWebViewDataListener {

    private WebView webView;
    private JSONObject jsonObject;

    /**
     * 模拟的数据
     */
    private String[] timeArr={"04-01","04-03","04-20"};
    private int[] priceArr={100,300,200};
    private int[] numArr={1,3,5};
    private int[] fairArr={200,400,600};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 这里模拟数据
        setData();

        webView = findViewById(R.id.wv_linechart);
        firstView();

    }

   private void setData(){
       // 这里模拟数据
       jsonObject=new JSONObject();
       JSONArray timeArray= new JSONArray();
       JSONArray priceArray= new JSONArray();
       JSONArray numArray= new JSONArray();
       JSONArray fairArray= new JSONArray();
       int length = timeArr.length;
       for (int i = 0; i < length; i++) {
           timeArray.put(timeArr[i]);
           priceArray.put(priceArr[i]);
           numArray.put(numArr[i]);
           fairArray.put(fairArr[i]);
       }
       try {
           jsonObject.put("timeArray", timeArray);
           jsonObject.put("priceArray", priceArray);
           jsonObject.put("numArray", numArray);
           jsonObject.put("fairArray",fairArray);
       } catch (JSONException e) {
           e.printStackTrace();
       }
   }

    private void firstView(){
        webView.loadUrl("file:///android_asset/index.html");
        Methods methods=new Methods();
        methods.setOnWebViewDataListener(this);
        // 调用注册方法，将java方法暴露出去。
        JSBridge.register("JSBridge", Methods.class);
        // 使用自定义的
        webView.setWebChromeClient(new JSBridgeChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        WebSettings webSettings=webView.getSettings();
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDisplayZoomControls(false);

    }

    @Override
    public void onWebViewDataListener(CallBack callBack) {
        callBack.apply(jsonObject);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (webView.canGoBack()){
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
