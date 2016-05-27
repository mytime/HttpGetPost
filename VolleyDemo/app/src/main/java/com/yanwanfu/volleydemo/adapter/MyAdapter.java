package com.yanwanfu.volleydemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yanwanfu.volleydemo.R;
import com.yanwanfu.volleydemo.bean.Book;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MyAdapter extends BaseAdapter {
    private Context c;
    private ArrayList<Book> list;


    public MyAdapter(Context context, ArrayList<Book> books){
        this.c = context;
        this.list = books;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder = null;
        if (holder == null){
            convertView = View.inflate(c, R.layout.item_list,null);
            holder=new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Book b = list.get(position);
        holder.tv.setText("评分:"+b.getMax()+"\n"+"评论:"+b.getNumRaters());
        return convertView;
    }

    class ViewHolder{
        TextView tv;
    }
}
