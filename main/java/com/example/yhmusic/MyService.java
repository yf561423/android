package com.example.yhmusic;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private List<Integer> playingMusicList;
    private Music currentMusic;
    private List<OnStateChangeListenr> listenrList;
    private int currentMusicIndex;
    private boolean isPlaying;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        playingMusicList = new ArrayList<Integer>();
        currentMusicIndex = 0;
        isPlaying = false;
        playingMusicList.add(R.raw.music1);
        playingMusicList.add(R.raw.music1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyServiceBinder();
    }

    public class MyServiceBinder extends Binder {

        public MyServiceBinder() {
            listenrList=new ArrayList<>();
        }

        public void play() {
            Log.d("yyh","playmusic");
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    isPlaying = false;
                } else {
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(getApplicationContext(), Uri.parse("android.resource://" + getPackageName() + "/" + playingMusicList.get(currentMusicIndex)));
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        isPlaying = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void stop() {
            Log.d("yyh","stopmusic");
            if (mediaPlayer != null && isPlaying) {
                mediaPlayer.stop();
                isPlaying = false;
            }
        }

        public void next() {
            Log.d("yyh","nextmusic");
            currentMusicIndex++;
            if (currentMusicIndex >= playingMusicList.size()) {
                currentMusicIndex = 0;
            }
            play();
        }

        public void previous() {
            Log.d("yyh","previousmusic");
            currentMusicIndex--;
            if (currentMusicIndex < 0) {
                currentMusicIndex = playingMusicList.size() - 1;
            }
            play();
        }

        public void addMusic(int musicId) {
            playingMusicList.add(musicId);
        }

        public void playOrPause() {
            if(mediaPlayer.isPlaying()==false)
                play();
            else
                stop();
        }

        public List<Integer> getPlayingList() {
                    return playingMusicList;
        }

        public void registerOnStateChangeListener(MyService.OnStateChangeListenr listenr) {

            listenrList.add(listenr);
        }

        public Music getCurrentMusic() {
            return currentMusic;
        }

        public void unregisterOnStateChangeListener(MyService.OnStateChangeListenr listenr) {
            listenrList.remove(listenr);
        }

        public void seekTo(int progress) {
            mediaPlayer.seekTo(progress);
        }

        public boolean isPlaying() {
            return mediaPlayer.isPlaying();
        }
    }

    public interface  OnStateChangeListenr {
        void onPlayProgressChange(long played, long duration);  //播放进度变化
        void onPlay(Music item);    //播放状态变化
        void onPause();   //播放状态变化

    }
}
