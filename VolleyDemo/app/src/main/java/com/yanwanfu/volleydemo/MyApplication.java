package com.yanwanfu.volleydemo;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 *  请求队列
 *  注意：
 *      需要注册到Androidmanifast.xml清单中的application标签内
 *      android:name=".MyApplication"
 *      同时添加网络访问权限
 */
public class MyApplication extends Application {
    public static RequestQueue queue; //请求对列
    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(getApplicationContext());
    }

    //向外部公开请求队列
    public static RequestQueue getHttpQueue(){
        return queue;
    }

}
