package com.yanwanfu.asynchttpdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        asyncHttpGet();
        //asyncHttpPost();
    }

    private void asyncHttpGet() {
        //1 创建请求客服端
        AsyncHttpClient client = new AsyncHttpClient();
        //
        String url = "http://apis.juhe.cn/mobile/get?phone=13500003157&key=144652e1ce4d2dd359b254e578078f5a";
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                //请求成功
//                Toast.makeText(MainActivity.this,headers.toString(),Toast.LENGTH_LONG).show();
                System.out.println(">>>>>>>>>>>>>>f"+statusCode);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                //请求失败
                Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_LONG).show();
            }
        });


    }
}
