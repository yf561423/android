package com.example.yhmusic;

import org.litepal.crud.LitePalSupport;

public class MyMusicDB extends LitePalSupport {
    public String music_name;     //歌曲名


    public MyMusicDB(String name) {
        this.music_name = name;
    }
}
