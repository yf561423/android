package com.example.yhmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class PlayingMusicAdapter extends BaseAdapter {

    List<Integer> myData;
    LayoutInflater myInflater;
    int   myResource;
    Context myContext;
    onDeleteButtonListener myonDeleteButtonListener;

    public PlayingMusicAdapter(Context context, int resId, List<Integer> data)
    {
        myContext = context;
        myData = data;
        myInflater = LayoutInflater.from(context);
        myResource = resId;
    }

    @Override
    public int getCount() {
        return myData != null ? myData.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return myData != null ? myData.get(position): null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Integer item = myData.get(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = myInflater.inflate(myResource, parent, false);
            holder = new ViewHolder();
            holder.music_name = view.findViewById(R.id.playing_title);
            holder.artist = view.findViewById(R.id.playing_artist);
            holder.delete = view.findViewById(R.id.delete);
            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.music_name.setText("音乐");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myonDeleteButtonListener.onClick(position);
            }
        });
        return view;
    }
    class ViewHolder{
        TextView music_name;
        TextView artist;
        ImageView delete;
    }
    public interface onDeleteButtonListener {
        void onClick(int i);
    }
    public void setOnDeleteButtonListener(onDeleteButtonListener myonDeleteButtonListener) {
        this.myonDeleteButtonListener = myonDeleteButtonListener;
    }

}
