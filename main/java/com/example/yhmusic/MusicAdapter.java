package com.example.yhmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MusicAdapter extends BaseAdapter {
    List<Music> myData;
    LayoutInflater myInflater;
    int myResource;
    onMoreButtonListener myonMoreButtonListener;

    public MusicAdapter(Context context, int resId, List<Music> data)
    {
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
        return myData != null ? myData.get(position): null ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Music item = myData.get(position);
        View view;
        ViewHolder holder;

        if (convertView == null) {
            view = myInflater.inflate(myResource, parent, false);
            holder = new ViewHolder();
            holder.music_name = view.findViewById(R.id.music_title);
            holder.artist = view.findViewById(R.id.music_artist);
            holder.more = view.findViewById(R.id.more);
            view.setTag(holder);
        }
        else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.music_name.setText(item.music_name);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myonMoreButtonListener.onClick(position);
            }
        });
        return view;
    }
    class ViewHolder{
        TextView music_name;
        TextView artist;
        LinearLayout more;
    }

    public interface onMoreButtonListener {
        void onClick(int i);
    }

    public void setOnMoreButtonListener(onMoreButtonListener myonMoreButtonListener) {
        this.myonMoreButtonListener = myonMoreButtonListener;
    }

}
