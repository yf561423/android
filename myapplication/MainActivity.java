package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Fragment fragment1,fragment2,fragment3,fragment4;
    FragmentManager fm;
    LinearLayout linearlayout1,linearlayout2,linearlayout3,linearlayout4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);

        fragment1 = new BlankFragment();
        fragment2 = new BlankFragment2();
        fragment3 = new BlankFragment3();
        fragment4 = new BlankFragment4();

        linearlayout1=findViewById(R.id.LinearLayout1);
        linearlayout2=findViewById(R.id.LinearLayout2);
        linearlayout3=findViewById(R.id.LinearLayout3);
        linearlayout4=findViewById(R.id.LinearLayout4);

        fm=getSupportFragmentManager();

        initial();
        fragment_hide();
        fragment_show(fragment1);

        linearlayout1.setOnClickListener(this);
        linearlayout2.setOnClickListener(this);
        linearlayout3.setOnClickListener(this);
        linearlayout4.setOnClickListener(this);
    }

    private void fragment_show(Fragment fragment) {
        FragmentTransaction transaction=fm.beginTransaction()
                .show(fragment);
        transaction.commit();
    }

    private void fragment_hide() {
        FragmentTransaction ft=fm.beginTransaction()
                .hide(fragment1)
                .hide(fragment2)
                .hide(fragment3)
                .hide(fragment4);
        ft.commit();
    }

    private void initial() {
        FragmentTransaction ft=fm.beginTransaction()
                .add(R.id.content,fragment1)
                .add(R.id.content,fragment2)
                .add(R.id.content,fragment3)
                .add(R.id.content,fragment4)
                ;
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        fragment_hide();
        int i=view.getId();
        if (i==R.id.LinearLayout1)
            fragment_show(fragment1);
        else if (i==R.id.LinearLayout2)
            fragment_show(fragment2);
        else if (i==R.id.LinearLayout3)
            fragment_show(fragment3);
        else if (i==R.id.LinearLayout4)
            fragment_show(fragment4);

    }

}