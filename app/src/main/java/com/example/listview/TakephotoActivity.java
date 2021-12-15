package com.example.listview;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TakephotoActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton ima1;
    Button btnuplod;
    public static final File SDPATH = Environment.getExternalStorageDirectory() ;
    EditText item;
    EditText sub;
    Bitmap bitmap;
    TextView messagelocation;
    ImageButton btn_getlocation;
    String uploadFile;
    String actionUrl="http://49.235.134.191:8080/file/image/upload";



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photos);
        ima1=findViewById(R.id.img_photo);
        ima1.setOnClickListener(this);
        messagelocation=findViewById(R.id.message_location);
        btnuplod=findViewById(R.id.btn_upload);
        btnuplod.setOnClickListener(this);
        item=findViewById(R.id.ett_item);
        sub=findViewById(R.id.ett_sub);
        btn_getlocation=findViewById(R.id.get_location);
        btn_getlocation.setOnClickListener(this);
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
                intent = new Intent();
                //MediaStore.ACTION_IMAGE_CAPTURE  调用系统的照相机
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0x3);
                break;
            case R.id.btn_upload:
//                String end = "/r/n";
//                String Hyphens = "--";
//                String boundary = "*****";
//                try
//                {
//                    URL url = new URL(actionUrl);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    /* 允许Input、Output，不使用Cache */
//                    con.setDoInput(true);
//                    con.setDoOutput(true);
//                    con.setUseCaches(false);
//                    /* 设定传送的method=POST */
//                    con.setRequestMethod("POST");
//                    /* setRequestProperty */
//                    con.setRequestProperty("Connection", "Keep-Alive");
//                    con.setRequestProperty("Charset", "UTF-8");
//                    con.setRequestProperty("Content-Type",
//                            "multipart/form-data;boundary=" + boundary);
//                    /* 设定DataOutputStream */
//                    DataOutputStream ds = new DataOutputStream(con.getOutputStream());
//                    ds.writeBytes(Hyphens + boundary + end);
//                    ds.writeBytes("Content-Disposition: form-data; "
//                            + "name=/"file1/";filename=/"" + newName + "/"" + end);
//                    ds.writeBytes(end);
//                    /* 取得文件的FileInputStream */
//                    FileInputStream fStream = new FileInputStream(uploadFile);
//                    /* 设定每次写入1024bytes */
//                    int bufferSize = 1024;
//                    byte[] buffer = new byte[bufferSize];
//                    int length = -1;
//                    /* 从文件读取数据到缓冲区 */
//                    while ((length = fStream.read(buffer)) != -1)
//                    {
//                        /* 将数据写入DataOutputStream中 */
//                        ds.write(buffer, 0, length);
//                    }
//                    ds.writeBytes(end);
//                    ds.writeBytes(Hyphens + boundary + Hyphens + end);
//                    fStream.close();
//                    ds.flush();
//                    /* 取得Response内容 */
//                    InputStream is = con.getInputStream();
//                    int ch;
//                    StringBuffer b = new StringBuffer();
//                    while ((ch = is.read()) != -1)
//                    {
//                        b.append((char) ch);
//                    }
//                    System.out.println("上传成功");
//                    Toast.makeText(TakephotoActivity.this, "上传成功", Toast.LENGTH_LONG)
//                            .show();
//                    ds.close();
//                } catch (Exception e)
//                {
//                    System.out.println("上传失败" + e.getMessage());
//                    Toast.makeText(TakephotoActivity.this, "上传失败" + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//
                /*Intent intent1 = new Intent(TakephotoActivity.this, Mainfaceactivity.class);
                Mainfaceactivity.bitmap = bitmap;
                Mainfaceactivity.ite = item.getText().toString().trim();
                Mainfaceactivity.sub = item.getText().toString().trim();
                startActivity(intent1);*/

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
            if (data != null) {
                Bundle bundle = data.getExtras();
                bitmap = bundle.getParcelable("data");
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                uploadFile = format.format(date);
                saveBitmap(bitmap,uploadFile);
                ima1.setImageBitmap(bitmap);
            } else {
                return;
            }
            if (requestCode == 0x4) {
                Log.v("aaaaa", String.valueOf(data));
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        //处理代码在此地
                        String str= bundle.getString("aaa");
                        messagelocation.setText(str);
                        //feedBack.setAddress(str);
                    }
                }
            }
        }

    }




    public String sdCardDir = Environment.getExternalStorageDirectory() + "/fingerprintimages/";
    public void saveBitmap(Bitmap bm, String picName) {
        Log.e("", "保存图片");
        Log.v("aaaaa", String.valueOf(getExternalFilesDir(null)));
        try {

            File f = new File(getExternalFilesDir(null), picName + ".JPEG");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }





}
