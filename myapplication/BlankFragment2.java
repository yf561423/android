package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BlankFragment2 extends Fragment {

    RecyclerView recyclerView;

    List list;

    Myadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view=inflater.inflate(R.layout.activity_recycle_view_main,container,false);
        recyclerView=view.findViewById(R.id.recyclerview);
        String[] names={"父亲", "母亲", "同学A", "同学B","同学C","同学D","同学E","同学F","同学G"};

        List<String> items=new ArrayList<String>();
        for(int i=0;i< names.length;i++)
        {
            items.add(names[i]);
        }
        Context context=this.getContext();

        adapter = new Myadapter(context,items);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager=new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        return view;
    }
}