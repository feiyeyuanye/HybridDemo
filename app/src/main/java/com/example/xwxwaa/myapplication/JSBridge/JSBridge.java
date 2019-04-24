package com.example.xwxwaa.myapplication.JSBridge;

import android.net.Uri;
import android.webkit.WebView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xwxwaa on 2018/7/26.
 */

public class JSBridge {
    /**
     * 用于存放所有暴露给JS层的类，和满足规则的方法。
      */
   public static Map<String,HashMap<String,Method>> exposeMethods=new HashMap<>();

    /**
     * 注册方法
     * @param exposeName 类名
     * @param myClass 类
     */
   public static void register(String exposeName, Class<?> myClass){
       if (!exposeMethods.containsKey(exposeName)){
           // 将所有验证过后的方法，放入表中。
           exposeMethods.put(exposeName,getAllMethod(myClass));
       }
   }

    /**
     * 获取一个类中，所有满足规则的方法。
     * @param injectedCls 类
     * @return 通过验证的方法
     */
   public static HashMap<String,Method> getAllMethod(Class injectedCls){
       HashMap<String,Method> methodHashMap=new HashMap<>();
       // 保存类中，所有声明过的方法。
       Method[] methods=injectedCls.getDeclaredMethods();
       // 遍历上面的数组
       for (Method method:methods){
           // 过滤规则
           // 1.方法必须为public
           // 2.方法必须为static
           // 3.name不为空
           if (method.getModifiers()!=(Modifier.PUBLIC| Modifier.STATIC)||method.getName()==null){
               // 如果不满足，则不继续后面的步骤。
               continue;
           }
           // 保存方法中的所有参数
           Class[] paramters=method.getParameterTypes();
           // 过滤规则
           // 1.参数不为空
           // 2.参数的个数为，3个。
           if (paramters!=null&&paramters.length == 3){
               // 过滤规则
               // 1.第一个参数为WebView
               // 2.第二个参数为JSONObject
               // 3.第三个参数为回调函数
               if (paramters[0] == WebView.class&&paramters[1]== JSONObject.class&&paramters[2] == CallBack.class){
                   // 参数类型符合
                   methodHashMap.put(method.getName(),method);
               }
           }
       }
       // 返回值
       return methodHashMap;
   }

    /**
     * 根据传入的Url，找到相应的类，相应的Java方法
     * @param webView WebView
     * @param urlString Url
     * @return
     */
    public static String callJava(WebView webView, String urlString){
        // 解析Url（类名。。方法名。。参数。。回调函数）
       String className;
       String methodName;
       String param;
       String port;
       if (!urlString.equals("")&&urlString!=null&&urlString.startsWith("JSBridge")){
           // 转成Uri
           Uri uri= Uri.parse(urlString);
           className=uri.getHost();
           methodName=uri.getPath().replace("/","");
           param=uri.getQuery();
           port=uri.getPort()+"";
           //  Log.e("TAG","uri;"+uri+"  className;"+className+"  methodName;"+methodName+"  param;"+param+"  port;"+port);
           //  如果类在表中
           if (exposeMethods.containsKey(className)){
               // 获取类中方法
               HashMap<String,Method> methodHashMap=exposeMethods.get(className);
               if (methodHashMap!=null&&methodHashMap.size()!=0&&methodHashMap.containsKey(methodName)){
                   // 要寻找的方法，存在于表中。
                   // 获取该方法
                   Method method=methodHashMap.get(methodName);
                   if (method!=null){
                       // 方法不为空，则执行它。
                       try {
                           method.invoke(null, webView, new JSONObject(param), new CallBack(webView,port));
                       } catch (IllegalAccessException e) {
                           e.printStackTrace();
                       } catch (InvocationTargetException e) {
                           e.printStackTrace();
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }
                   }
               }
           }
       }
       return null;
    }

}
