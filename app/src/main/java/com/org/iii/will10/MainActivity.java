package com.org.iii.will10;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        sp = getSharedPreferences("gameDate", MODE_PRIVATE);
        editor = sp.edit();
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
}
