package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

public class jinduview extends AppCompatActivity {
    public static int feed_back_id=20;
    private String[] titles={};
    private String[] detailss={};
    private String[] dates={};
    Bitmap[]bitmaps={};
    private String[] leibies={};
    private String[] jindus={};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jinduview);
        ListView listview=findViewById(R.id.lvTrace);
        gethistoryDetails();
        MyBaseAdapter adapter=new MyBaseAdapter();
        listview.setAdapter(adapter);
    }

    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int i) {
            return titles[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            View view=View.inflate(jinduview.this,R.layout.item_trace,null);
            TextView title=view.findViewById(R.id.title);
            TextView details=view.findViewById(R.id.details);
            TextView date1=view.findViewById(R.id.time);

            title.setText(titles[i]);
            details.setText(detailss[i]);
            date1.setText(dates[i]);


            return view;
        }
    }

    private void gethistoryDetails() {
        try{
            String path="http://49.235.134.191:8080/feedback/process?feed_back_id="+feed_back_id;
            URL url=new URL(path);
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            if (conn.getResponseCode()==200) {
                InputStream in = conn.getInputStream();
                String jsonStr = "";
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                jsonStr = new String(out.toByteArray());
                Result result = JSONObject.parseObject(jsonStr, Result.class);
                if (result.getCode() == 200) {

                    Trace historyDetails;
                    JSONArray hisJsonArray=JSONArray.parseArray(result.getData().toString());
                    Log.v("mybug", hisJsonArray.toString());
                    for(int i=0;i<hisJsonArray.size();i++) {
                        JSONObject hisJsonObject=hisJsonArray.getJSONObject(i);
                        historyDetails = JSON.toJavaObject(hisJsonObject,Trace.class);
                        detailss=insert(detailss,historyDetails.getImageUrl());
                        titles=insert(titles,historyDetails.getDesc());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dates=insert(dates,dateFormat.format(historyDetails.getTime()));


                    }
                }
                else {
                    Toast.makeText(jinduview.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String[] insert(String[] arr, String str) {
        int size = arr.length;  //获取数组长度
        String[] tmp = new String[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++){  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = str;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }
}