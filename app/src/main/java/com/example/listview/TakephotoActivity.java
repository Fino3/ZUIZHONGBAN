package com.example.listview;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.alibaba.fastjson.JSONObject;
import com.baidu.platform.comapi.util.JsonBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class TakephotoActivity extends AppCompatActivity implements View.OnClickListener {

    public static String account;

    ImageButton ima1;
    Button btnuplod;
    EditText item;
    EditText sub;
    TextView messagelocation;
    ImageButton btn_getlocation;
    String mFilePath;
    RadioButton rb_safe;
    RadioButton rb_clear;
    RadioButton rb_zhixu;
    RadioButton rb_imp;
    RadioButton rb_nor;

    private FileInputStream is = null;

    private static final int TIMEOUT_IN_MILLIONS = 5000;
    private static final String CHARSET = "utf-8";
    private static final String BOUNDARY = UUID.randomUUID().toString();
    private static final String PREFIX = "--";
    private static final String LINE_END = "\r\n";
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressLint("NewApi")
    FeedBack feedBack=new FeedBack();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photos);
        rb_safe=findViewById(R.id.rb_safe);
        rb_clear=findViewById(R.id.rb_clear);
        rb_zhixu=findViewById(R.id.rb_zhixu);
        rb_imp=findViewById(R.id.rb_imp);
        rb_nor=findViewById(R.id.rb_nor);
        ima1=findViewById(R.id.img_photo);
        ima1.setOnClickListener(this);
        messagelocation=findViewById(R.id.message_location);
        btnuplod=findViewById(R.id.btn_upload);
        btnuplod.setOnClickListener(this);
        item=findViewById(R.id.ett_item);
        sub=findViewById(R.id.ett_sub);
        btn_getlocation=findViewById(R.id.get_location);
        btn_getlocation.setOnClickListener(this);
        Log.v("aaaaa", getExternalFilesDir(null) + "/" + "mytest.png");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_photo:
                Intent intent;
                if (Build.VERSION.SDK_INT >= 23) {
                    int checkPermission = ContextCompat.checkSelfPermission(TakephotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                        (new AlertDialog.Builder(TakephotoActivity.this)).setMessage("您需要在设置里打开存储空间权限。").setPositiveButton("确认", new android.content.DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(TakephotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                            }
                        }).setNegativeButton("取消", (android.content.DialogInterface.OnClickListener) null).create().show();
                        return;
                    }
                }
                mFilePath=getExternalFilesDir(null) + "/" + "mytest.png";
                File cameraPhoto = new File(mFilePath);
                /*takePhotoBiggerThan7((new File(mFilePath)).getAbsolutePath());*/
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);;
                //MediaStore.ACTION_IMAGE_CAPTURE  调用系统的照相机
                Uri photoUri = FileProvider.getUriForFile(
                        this,
                        getPackageName() + ".fileprovider",
                        cameraPhoto);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent, 0x3);
                break;
            case R.id.btn_upload:
                Result result=new Result();
                Toast.makeText(TakephotoActivity.this,"uploud",Toast.LENGTH_SHORT).show();
                Log.v("aaaaa","upload");
                mFilePath=getExternalFilesDir(null) + "/" + "mytest.png";
                File file=new File(mFilePath);
                try {
                    result = uploadFile(file,"http://49.235.134.191:8080/file/image/upload");
                    //Log.v("aaaaa", result.getData().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                feedBack.setImageUrl(result.getData().toString());
                Log.v("aaaaa",feedBack.getImageUrl());
                feedBack.setTitle(item.getText().toString());
                feedBack.setDesc(sub.getText().toString());
                feedBack.setAccount(account);
                if (rb_safe.isChecked()) {
                    feedBack.setCategory("安全隐患");
                } else if (rb_clear.isChecked()) {
                    feedBack.setCategory("卫生问题");
                } else if (rb_zhixu.isChecked()) {
                    feedBack.setCategory("秩序问题");
                }
                if (rb_imp.isChecked()) {
                    feedBack.setDegree(1);
                } else if (rb_nor.isChecked()) {
                    feedBack.setDegree(0);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Date date=new Date(System.currentTimeMillis());
                feedBack.setTime(date);
                Log.v("aaaaa",feedBack.getImageUrl()+feedBack.getAccount()+feedBack.getAddress()+"aaaaaaaaaaaaaaaaaaaaaaa");

                try {
                    uploadFeedback(feedBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.get_location:
                Intent intent2 = new Intent();
                intent2.setClass(TakephotoActivity.this,Location.class);
                Bundle bundle = new Bundle();
                intent2.putExtras(bundle);
                startActivityForResult(intent2, 0x4);
                break;

        };

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0x3) {
            try {
                mFilePath=getExternalFilesDir(null) + "/" + "mytest.png";
                // 获取输入流
                is = new FileInputStream(mFilePath);
                // 把流解析成bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                // 设置图片
                ima1.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                // 关闭流
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
        if (requestCode == 0x4) {
            Log.v("aaaaa", String.valueOf(data.getExtras()));
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    //处理代码在此地
                    String str= bundle.getString("aaa");
                    messagelocation.setText(str);
                    feedBack.setAddress(str);
                }
            }
        }
    }
    public static Result uploadFile(File file, String RequestURL) throws Exception{
        String CONTENT_TYPE = "multipart/form-data";
        URL url = new URL(RequestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
        conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
        conn.setDoInput(true); //允许输入流
        conn.setDoOutput(true); //允许输出流
        conn.setUseCaches(false); //不允许使用缓存
        conn.setRequestMethod("POST"); //请求方式
        conn.setRequestProperty("Charset", "utf-8");
        //设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        if (file != null) {
            Log.v("aaaaa","file != null");
            /** * 当文件不为空，把文件包装并且上传 */
            OutputStream outputSteam = conn.getOutputStream();
            DataOutputStream dos = new DataOutputStream(outputSteam);
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
            Log.v("aaaaa",file.getName());
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            dos.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            /**
             * 获取响应码 200=成功
             * 当响应成功，获取响应的流
             */
            int res = conn.getResponseCode();
            if (res == 200) {
                Log.v("aaaaa","success");
                InputStream inputStream = conn.getInputStream();

                String input;
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                while ((input = reader.readLine()) != null) {
                    stringBuffer.append(input);
                }
                return JSONObject.parseObject(stringBuffer.toString(), Result.class);
            } else {
                Log.v("aaaaa", conn.getResponseMessage());
            }
        }
        throw new RuntimeException("文件不存在");
    }

    public void uploadFeedback(FeedBack feedback) throws Exception {
        Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
        String json=gson.toJson(feedBack);
        Log.v("aaaaa",json);
        URL url = new URL("http://49.235.134.191:8080/feedback/save");
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = FormBody.create(MediaType.parse("application/json; charset=utf-8")
                , json);
        Request request = new Request.Builder()
                .url(url)//请求的url
                .post(requestBody)
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
                         @Override
                         public void onFailure(okhttp3.Call call, IOException e) {

                         }

                         @Override
                         public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                             final String res = response.body().string();
                             if (response.code() == 200) {
                                 Log.v("aaaaa","上传成功");
                                 //Toast.makeText(TakephotoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                                 finish();
                             }
                         }
                     });
    }
        /*HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
        conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
        conn.setDoInput(true); //允许输入流
        conn.setDoOutput(true); //允许输出流
        //conn.setUseCaches(false); //不允许使用缓存
        conn.setRequestMethod("POST"); //请求方式
        conn.setRequestProperty("Charset", "utf-8");
        //设置编码
        conn.setRequestProperty("Content-Type", "application/json");
        Log.v("aaaaa","conn.setRequestProperty");
        if (feedback != null) {
            DataOutputStream os = new DataOutputStream( conn.getOutputStream());
            os.writeBytes(json);
            os.flush();
            os.close();

            int res = conn.getResponseCode();
            Log.v("aaaaa","code:"+String.valueOf(res));
            Log.v("aaaaa", conn.getResponseMessage());
            if (res == 200) {
                InputStream in = conn.getInputStream();
                String jsonStr = "";
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer, 0, buffer.length)) != -1) {
                    out.write(buffer, 0, len);
                }
                jsonStr = new String(out.toByteArray());
                Log.v("mybug",jsonStr);
                Result result = JSONObject.parseObject(jsonStr, Result.class);
                Log.v("debug",result.toString());
                if (result.getCode()==200) {
                    Log.v("aaaaa","success");
                    Toast.makeText(TakephotoActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                    conn.disconnect();
                }
                else if (result.getCode()==400) {
                    Toast.makeText(TakephotoActivity.this,result.getMessage(),Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.v("aaaaa", conn.getResponseMessage());
            }*/


}
