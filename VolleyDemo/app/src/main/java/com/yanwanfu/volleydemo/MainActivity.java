package com.yanwanfu.volleydemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.yanwanfu.volleydemo.adapter.MyAdapter;
import com.yanwanfu.volleydemo.bean.Book;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/**
 * 两个知识点：
 * 1 Volley 的Get Post请求方式
 * 2 Volley 的网络请求队列建立和取消队列请求及Activity周期
 */
public class MainActivity extends Activity {
    private final String TAG = "MainActivity";
    private ImageView iv1;
    private NetworkImageView iv2;
    private ListView lv1;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    private void init() {
        //iv1 = (ImageView) findViewById(R.id.iv1);
        //iv2 = (NetworkImageView) findViewById(R.id.iv2);
        lv1 = (ListView) findViewById(R.id.lv1);

        volley_get();
        //volley_post();          //post请求网络数据
        //volley_loader_image();  //请求网络图片
        //volley_loader_NetWorkImageView();//请求网络图片2
    }

    /**
     * POST请求方法
     */
    private void volley_post() {
        String url = "http://apis.juhe.cn/mobile/get?";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
              //输出网络获取的信息

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
        request.setTag("abcPost");                  //取消队列时调用 加入到Activity生命周期
        MyApplication.getHttpQueue().add(request);    //添加到对列
    }

    /**
     * GET请求方法
     */
    private void volley_get() {
        //StringRequest参数，
        // arg1:请求方法：get post
        // arg2:请求的url
        // arg3: 请求监听
        // arg4: 请求错误处理
        // url:查询号码归属地

        String url = "https://api.douban.com/v2/book/1220562";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //请求成功返回的数据
                Log.i("info",response);
                try {
                    dealData(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                // 请求失败返回的数据
                Toast.makeText(MainActivity.this, "网络错误.", Toast.LENGTH_LONG).show();
            }
        });
        request.setTag("abcGet"); //调用这个标签可以停止网络请求 加入到Activity生命周期
        MyApplication.getHttpQueue().add(request);//将请求加入到队列里面
    }

    private void dealData(String s) throws JSONException {
        //创建一个Gson
        Gson gson = new Gson();
        JSONObject object = new JSONObject(s);
        Log.i("info",object.getString("rating"));
        String rating = object.getString("rating");

        //解析字符串
        Book book = gson.fromJson(rating, Book.class);
        Log.i("info","最大评级："+book.getMax());
        ArrayList<Book> list = new ArrayList<Book>();
        list.add(book);


//        ArrayList<Phone> phones = gson.fromJson(
//                object.getString("result"),
//                new TypeToken<Phone>(){}.getType());
//        Log.i("phones",phones.toString());
        adapter = new MyAdapter(this,list);
        lv1.setAdapter(adapter);
    }

    /**
     * 图片请求方法一：图片加载到ImageView
     */
    private void volley_loader_image(){
        //图片地址
        String imageurl =  "http://www.chebao360.com/images_3/logo.png";
        //创建缓存
        final LruCache<String ,Bitmap> lruCache = new LruCache<String,Bitmap>(20);
        //创建图片缓存， ImageCache 是个接口，需要复写他的两个方法
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url_key) {
                return lruCache.get(url_key);//从缓存读取
            }

            @Override
            public void putBitmap(String url_key, Bitmap bitmap_value) {
                lruCache.put(url_key,bitmap_value);//写入缓存
            }
        };
        //加载图片,参数1：RequestQueue,参数2:图片缓存
        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueue(),imageCache);
        // 创建图片监听器
        ImageLoader.ImageListener listener = imageLoader.getImageListener(
                iv1,                         //需要加载图片的容器
                R.mipmap.ic_launcher,       //默认显示的图片
                R.mipmap.ic_launcher);      //发生错误时显示的图片

        imageLoader.get(imageurl,listener);   //加载图片

    }
    /**
     * 方法二 使用NetworkImageView显示图片
     */
    public void volley_loader_NetWorkImageView() {
        //图片地址
        String imageurl = "https://www.baidu.com/img/bd_logo1.png";
        //创建缓存
        final LruCache<String, Bitmap> lurcache = new LruCache<String, Bitmap>(20);
        //创建图片缓存 ImageCache 是个接口，需要复写他的两个方法
        ImageLoader.ImageCache imgCache = new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url_Key) {
                return lurcache.get(url_Key); //抛出缓存里的url_Key
            }

            @Override
            public void putBitmap(String url_key, Bitmap bitmap_value) {
                //写进缓存
                lurcache.put(url_key, bitmap_value);
            }
        };
        ImageLoader imageLoader = new ImageLoader(
                MyApplication.getHttpQueue(),      //对列
                imgCache);  //图片缓存
        iv2.setTag("url"); //终止网络连接时调用 加入到Activity生命周期
        iv2.setImageUrl(imageurl, imageLoader);

    }

    @Override
    protected void onStop() {
        super.onStop();
        MyApplication.getHttpQueue().cancelAll("abcPost");  //终止网络连接
        MyApplication.getHttpQueue().cancelAll("abcGet");   //终止网络连接
        MyApplication.getHttpQueue().cancelAll("url");
    }
}
