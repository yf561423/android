package com.example.yhmusic;

import org.litepal.crud.LitePalSupport;

public class PlayingMusicDB extends LitePalSupport{
    public String music_name;     //歌曲名



    public PlayingMusicDB(String name) {
        this.music_name = name;
    }
}
