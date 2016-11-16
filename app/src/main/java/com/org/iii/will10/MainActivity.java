package com.org.iii.will10;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView textView;
    private File sdroot, approot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        sp = getSharedPreferences("gameDate", MODE_PRIVATE);
        editor = sp.edit();

//        String state = Environment.getExternalStorageState();
        sdroot = Environment.getExternalStorageDirectory();
        Log.v("will", sdroot.getAbsolutePath());

        // 以下是危險權限
        if ((ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    }, 123);
        } else {
            init();
        }
    }

    private void init() {
        approot = new File(sdroot, "Android/data/" + getPackageName());
        if (!approot.exists()) approot.mkdirs();
    }

    // callback

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Log.v("will", "OK");
                init();
            }
        }
    }

    public void test1(View v){
        editor.putInt("stage", 3);
        editor.putString("user", "Brad");
        editor.commit();
        Toast.makeText(this, "Save OK", Toast.LENGTH_SHORT).show();
    }

    public void test2(View v) {
        int stage = sp.getInt("stage", 0);
        String user = sp.getString("user", "Guest");
        textView.setText("stage:" + stage + "\n" +
                        "user:" + user);
    }

    // 內存 write
    public void test3(View v) {
        try {
            FileOutputStream fout = openFileOutput("will.data", MODE_PRIVATE);
            fout.write("hello.world\nhello.will\n12345\n你好".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "Save OK", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.v("will", e.toString());
        }
    }
    // 內存 read
    public void test4(View v) {
        textView.setText("");
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(openFileInput("will.data")));
            String line;
            while ( (line = reader.readLine()) != null) {
                textView.append(line + "\n");
            }

            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // sd write
    public void test5(View v) {
        try {
            FileOutputStream fout = new FileOutputStream(
                    new File(sdroot, "test.txt"));
            fout.write("Hello!test1".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "Save1 ok", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.v("will", "test5() : " + e.toString());
        }
    }

    public void test6(View v) {
        try {
            FileOutputStream fout = new FileOutputStream(
                    new File(approot, "test2.txt"));
            fout.write("Hello!test2".getBytes());
            fout.flush();
            fout.close();
            Toast.makeText(this, "Save2 ok", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Log.v("will", "test6() : " + e.toString());
        }
    }

    public void test7(View v) {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(new File(approot, "test2.txt")));
            String line;
            if ( (line = reader.readLine()) != null) {
                textView.append(line + "\n");
            }
            reader.close();
        } catch (Exception e) {
            Log.v("will", "test7():" + e.toString());
        }
    }
}
