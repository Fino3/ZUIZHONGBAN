package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    public static String account;
    private String[] titles={};
    private String[] detailss={};
    private String[] dates={};
    Bitmap[]bitmaps={};
    private String[] leibies={};
    private String[] jindus={};
    private Button btnjindu;
    private int []id={};
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ListView listview=findViewById(R.id.lv1);
        gethistoryDetails();
        MyBaseAdapter adapter=new MyBaseAdapter();
        listview.setAdapter(adapter);
    }

    private void gethistoryDetails() {
        try{
            String path="http://49.235.134.191:8080/feedback/get?account="+account;
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

                    HistoryDetails historyDetails;
                    JSONArray hisJsonArray=JSONArray.parseArray(result.getData().toString());
                    Log.v("mybug", hisJsonArray.toString());
                    for(int i=0;i<hisJsonArray.size();i++) {
                        JSONObject hisJsonObject=hisJsonArray.getJSONObject(i);
                        historyDetails = JSON.toJavaObject(hisJsonObject,HistoryDetails.class);
                        Log.v("mybug",historyDetails.getTitle());
                        titles=insert(titles,historyDetails.getTitle());
                        detailss=insert(detailss,historyDetails.getDesc());
                        leibies=insert(leibies,historyDetails.getCategory());
                        jindus=insert(jindus,historyDetails.getProcess());
                        id=insert(id,historyDetails.getId());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        dates=insert(dates,dateFormat.format(historyDetails.getTime()));
                        Bitmap bitmap1 = getHttpBitmap(historyDetails.getImageUrl());
                        bitmaps=insert(bitmaps,bitmap1);
                    }
                }
                else {
                    Toast.makeText(HistoryActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyBaseAdapter extends BaseAdapter implements View.OnClickListener {


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
            View view=View.inflate(HistoryActivity.this,R.layout.fb_item,null);
            TextView title=view.findViewById(R.id.title);
            TextView details=view.findViewById(R.id.details);
            TextView date1=view.findViewById(R.id.date1);
            ImageView iv=view.findViewById(R.id.iv);
            TextView leibie=view.findViewById(R.id.leibie);
            TextView jindu=view.findViewById(R.id.jindu);
            Button btnjindu=view.findViewById(R.id.btn_jindu);
            btnjindu.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //Toast.makeText(HistoryActivity.this,id[i],Toast.LENGTH_SHORT).show();
                                                Log.e("TAG","匿名内部类实现的点击事件");
                                                Intent intent2=new Intent();
                                                intent2.setClass(getApplicationContext(),jinduview.class);
                                                jinduview.feed_back_id=id[i];
                                                startActivity(intent2);
                                            }
                                        });
            title.setText(titles[i]);
            details.setText(detailss[i]);
            date1.setText(dates[i]);
            iv.setImageBitmap(bitmaps[i]);
            leibie.setText(leibies[i]);
            jindu.setText(jindus[i]);

            return view;
        }


        @Override
        public void onClick(View view) {

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

    private static int[] insert(int[] arr, int str) {
        int size = arr.length;  //获取数组长度
        int[] tmp = new int[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++){  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = str;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }

    private  static Bitmap[] insert(Bitmap[] arr, Bitmap bitmap) {
        int size = arr.length;  //获取数组长度
        Bitmap[] tmp = new Bitmap[size + 1];  //新建临时字符串数组，在原来基础上长度加一
        for (int i = 0; i < size; i++){  //先遍历将原来的字符串数组数据添加到临时字符串数组
            tmp[i] = arr[i];
        }
        tmp[size] = bitmap;  //在最后添加上需要追加的数据
        return tmp;  //返回拼接完成的字符串数组
    }
    public static Bitmap getHttpBitmap(String url){
        URL myFileURL;
        Bitmap bitmap=null;
        try{
            myFileURL = new URL(url);
            //获得连接
            HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
            //设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            //连接设置获得数据流
            conn.setDoInput(true);
            //不使用缓存
            conn.setUseCaches(false);
            //这句可有可无，没有影响
            //conn.connect();
            //得到数据流
            InputStream is = conn.getInputStream();
            //解析得到图片
            bitmap = BitmapFactory.decodeStream(is);
            Matrix matrix = new Matrix();
            matrix.setScale(0.2f, 0.2f);
            bitmap= Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            //关闭数据流
            is.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
}