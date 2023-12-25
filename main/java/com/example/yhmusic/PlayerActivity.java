package com.example.yhmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;
import java.util.Objects;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView btnPlayPre;
    TextView musicTitleView;
    ImageView btnPlayOrPause;
    ImageView btnPlayNext;
    ImageView btnPlayingList;
    TextView nowTimeView;
    TextView totalTimeView;
    SeekBar seekBar;
    private MyService.MyServiceBinder serviceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // 绑定 MyService 服务
        Intent serviceIntent = new Intent(this, MyService.class);
        bindService(serviceIntent, mServiceConnection, BIND_AUTO_CREATE);
        //初始化
        initActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
    }

    // 控件监听
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.play_pre) {
            // 上一首
            serviceBinder.previous();
        } else if (viewId == R.id.play_next) {
            // 下一首
            serviceBinder.next();
        } else if (viewId == R.id.play_or_pause) {
            // 播放或暂停
            serviceBinder.playOrPause();
        } else if (viewId == R.id.playing_list) {
            // 播放列表
            showPlayList();
        }
    }

    private void initActivity() {
        btnPlayOrPause = findViewById(R.id.play_or_pause);
        btnPlayPre = findViewById(R.id.play_pre);
        btnPlayNext = findViewById(R.id.play_next);
        btnPlayingList = findViewById(R.id.playing_list);
        seekBar = findViewById(R.id.seekbar);
        nowTimeView = findViewById(R.id.current_time);
        totalTimeView = findViewById(R.id.total_time);
        ImageView needleView = findViewById(R.id.ivNeedle);

        // ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        // 设置监听
        btnPlayOrPause.setOnClickListener(this);
        btnPlayPre.setOnClickListener(this);
        btnPlayNext.setOnClickListener(this);
        btnPlayingList.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //拖动进度条时
                nowTimeView.setText(Utils.formatTime((long) progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                serviceBinder.seekTo(seekBar.getProgress());
            }
        });

        // 绑定service
        Intent i = new Intent(this, MyService.class);
        bindService(i, mServiceConnection, BIND_AUTO_CREATE);
    }

    //显示当前正在播放的音乐
    private void showPlayList(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("播放列表");

        //获取播放列表
        final List<Integer> playingList = serviceBinder.getPlayingList();

        if(playingList.size() > 0) {
            //播放列表有曲目，显示所有音乐
            final PlayingMusicAdapter playingAdapter = new PlayingMusicAdapter(this, R.layout.playinglist_item, playingList);
        }
        else {
            //播放列表没有曲目，显示没有音乐
            builder.setMessage("没有正在播放的音乐");
        }
        builder.setCancelable(true);
        builder.create().show();
    }

    //定义与服务的连接的匿名类
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定成功后，取得MusicSercice提供的接口
            serviceBinder = (MyService.MyServiceBinder) service;

            //注册监听器
            serviceBinder.registerOnStateChangeListener(listenr);

            //获得当前音乐
            Music item = serviceBinder.getCurrentMusic();

            if(item == null) {
                //当前音乐为空, seekbar不可拖动
                seekBar.setEnabled(false);
            }
            else if (serviceBinder.isPlaying()){
                //如果正在播放音乐, 更新信息
                musicTitleView.setText(item.music_name);
                btnPlayOrPause.setImageResource(R.drawable.ic_pause);

            }
            else {
                //当前有可播放音乐但没有播放
                musicTitleView.setText(item.music_name);
                btnPlayOrPause.setImageResource(R.drawable.ic_play);
            }
        }

        public void onServiceDisconnected(ComponentName name) {
            //断开连接之后, 注销监听器
            serviceBinder.unregisterOnStateChangeListener(listenr);
        }
    };

    //实现监听器监听MusicService的变化，
    private MyService.OnStateChangeListenr listenr = new MyService.OnStateChangeListenr() {

        @Override
        public void onPlayProgressChange(long played, long duration) {
            seekBar.setMax((int) duration);
            totalTimeView.setText(Utils.formatTime(duration));
            nowTimeView.setText(Utils.formatTime(played));
            seekBar.setProgress((int) played);
        }

        @Override
        public void onPlay(final Music item) {
            //变为播放状态时
            musicTitleView.setText(item.music_name);
            btnPlayOrPause.setImageResource(R.drawable.ic_pause);
        }

        @Override
        public void onPause() {
            //变为暂停状态时
            btnPlayOrPause.setImageResource(R.drawable.ic_play);
            //rotateAnimator.pauseAnimator();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        //界面退出时的动画
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }
}
