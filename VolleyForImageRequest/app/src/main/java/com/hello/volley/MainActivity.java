package com.hello.volley;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

/**
 * Volley 图片请求
 */
public class MainActivity extends AppCompatActivity {
    private ImageView iv1;
    private NetworkImageView iv2;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (NetworkImageView) findViewById(R.id.iv2);

        loadImg();
        NetWorkImageViewVolley();
    }

    /**
     * 方法一 使用ImageView显示图片
     */
    public void loadImg() {
        //图片地址
        String imageurl =  "http://www.chebao360.com/images_3/logo.png";
        //创建队列
        queue = Volley.newRequestQueue(this);
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
        //加载图片 arg1:请求队列，arg2:图片缓存
        ImageLoader imageLoader = new ImageLoader(queue, imgCache);
        //创建图片监听器
        ImageLoader.ImageListener listener = imageLoader.getImageListener(
                iv1,                    //需要加载图片的容器
                R.mipmap.ic_launcher,   //默认显示的图片
                R.mipmap.ic_launcher);  //发生错误时显示的图片
        imageLoader.get(imageurl, listener);
    }

    /**
     * 方法二 使用NetworkImageView显示图片
     */
    public void NetWorkImageViewVolley() {
        //图片地址
        String imageurl = "https://www.baidu.com/img/bd_logo1.png";
        //创建队列
        queue = Volley.newRequestQueue(this);
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
                queue,      //对列
                imgCache);  //图片缓存
        iv2.setTag("url");
        iv2.setImageUrl(imageurl, imageLoader);

    }

    @Override
    protected void onStop() {
        super.onStop();
        queue.cancelAll("url");
    }
}
