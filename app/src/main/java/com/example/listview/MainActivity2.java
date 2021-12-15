package com.example.listview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {
    Button btn1;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btn1=findViewById(R.id.btn1);
        text=findViewById(R.id.text1);
        btn1.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
//                        String end = "/r/n";
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
//                    Toast.makeText(MainActivity2.this, "上传成功", Toast.LENGTH_LONG)
//                            .show();
//                    ds.close();
//                } catch (Exception e)
//                {
//                    System.out.println("上传失败" + e.getMessage());
//                    Toast.makeText(MainActivity2.this, "上传失败" + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
    }
}