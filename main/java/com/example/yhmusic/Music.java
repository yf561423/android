package com.example.yhmusic;

import java.util.Objects;

public class Music {
    public String music_name;      //歌曲名
    //public boolean isOnlineMusic;

    public Music(String name) {
        this.music_name = name;
    }

    //重写equals方法, 使得可以用contains方法来判断列表中是否存在Music类的实例
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return Objects.equals(music_name, music.music_name);
    }
}
