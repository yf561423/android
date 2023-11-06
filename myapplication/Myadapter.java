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
    List<Map<String,Object>> list1;
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
        String name=list1.get(position).get("my_name").toString();
        String phone=list1.get(position).get("my_phone").toString();
        String area=list1.get(position).get("my_area").toString();
        holder.textView.setText(name);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context1,Activity2.class);
                intent.putExtra("details",name);
                intent.putExtra("phone",phone);
                intent.putExtra("area",area);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //开始跳转
                context1.startActivity(intent);
            }
        });
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

