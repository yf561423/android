package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
        String[] phones={"11215875987","12345858585","12585858585",
                "13758919415","12578719852","15187974875",
                "54758989845","15255551328","15813565879"};
        String[] area={"湖北 武汉","湖北 武汉","湖北 武汉","湖北 武汉","湖北 武汉","湖北 武汉","湖北 武汉","湖北 武汉","湖北 武汉"};

        List<Map<String,Object>> items=new ArrayList<Map<String,Object>>();
        for(int i=0;i<names.length;i++)
        {
            Map<String,Object> item=new HashMap<String, Object>();
            item.put("my_name",names[i]);
            item.put("my_phone",phones[i]);
            item.put("my_area",area[i]);
            items.add(item);
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