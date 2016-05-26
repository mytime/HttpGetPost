package com.yanwanfu.volleydemo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 两个知识点：
 * 1 Volley 的Get Post请求方式
 * 2 Volley 的网络请求队列建立和取消队列请求及Activity周期
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //volley_get();
        volley_post();
    }

    private void volley_post() {
        String url = "http://apis.juhe.cn/mobile/get?";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //请求成功回调方法
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //请求失败返回方法
                Toast.makeText(MainActivity.this, "网络错误.", Toast.LENGTH_LONG).show();
            }
        }) {
            /**
             * 重写封装参数方法
             * @return map 参数集合
             */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //传出参数
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("phone", "13500003157");                         //参数1
                map.put("key", "144652e1ce4d2dd359b254e578078f5a");   //参数2
                return map;
            }
        };
        request.setTag("abcPost");                  //取消队列时调用
        MyApplication.getHttpQueue().add(request);    //添加到对列
    }

    private void volley_get() {
        //StringRequest参数，
        // arg1:请求方法：get post
        // arg2:请求的url
        // arg3: 请求监听
        // arg4: 请求错误处理
        // url:查询号码归属地
        String url = "http://apis.juhe.cn/mobile/get?phone=13500003157&key=144652e1ce4d2dd359b254e578078f5a";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //请求成功返回的数据
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 请求失败返回的数据
                Toast.makeText(MainActivity.this, "网络错误.", Toast.LENGTH_LONG).show();
            }
        });
        request.setTag("abcGet"); //调用这个标签可以停止网络请求
        MyApplication.getHttpQueue().add(request);//将请求加入到队列里面
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll("abcPost");  //终止网络连接
        MyApplication.getHttpQueue().cancelAll("abcGet");   //终止网络连接
    }
}
