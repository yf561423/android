package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {
    @Override
    protected void onPostResume() {
        super.onPostResume();


        Log.d("xr","2:onPostResume...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d("xr","2:onDestroy...");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("xr","2:onStart...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("xr","2:onResume...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("xr","2:onStop...");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("xr","2:onPause...");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("xr","2:onRestart...");
    }
    Button button;
    TextView name_details,textView1,textView2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);
        Intent intent=getIntent();
        button=findViewById(R.id.button_details);
        Log.d("xr","2:onCreate...");
        name_details=findViewById(R.id.name_detail);
        name_details.setText(intent.getStringExtra("details"));
        textView1=findViewById(R.id.phone_detail);
        textView1.setText(intent.getStringExtra("phone"));
        textView2=findViewById(R.id.area_detail);
        textView2.setText(intent.getStringExtra("area"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("results","888");
                setResult(666,intent);
                finish();
            }
        });
    }
}