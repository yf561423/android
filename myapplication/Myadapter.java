package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

public class Myadapter extends RecyclerView.Adapter<Myadapter.Myholder> {
    Context context1;
    List list1;
    public Myadapter(Context context,List list){
        context1=context;
        list1=list;
    }
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view=LayoutInflater.from(context1).inflate(R.layout.item,parent,false);
        Myholder holder=new Myholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Myadapter.Myholder holder, int position) {
        String name=list1.get(position).toString();
        holder.textView.setText(name);
    }

    @Override
    public int getItemCount() {
        return list1.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        TextView textView;
        public Myholder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView1);
        }
    }
}

