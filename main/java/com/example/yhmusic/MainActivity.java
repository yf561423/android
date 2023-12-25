package com.example.yhmusic;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    ListView musicListView;
    TextView playingTitleView;
    TextView playingArtistView;
    ImageView btnPlayOrPause;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView musicCountView;
    private static List<Music> musicList;
    private MusicAdapter musicAdapter;
    private SharedPreferences spf;
    MyService.MyServiceBinder serviceBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化活动
        initActivity();

        // 初始化音乐列表
        initMusicList();

        // 初始化配置
        initsettings();

        // 点击列表项播放音乐
        musicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = musicList.get(position);
            }
        });

}
    @Override
    protected void onResume() {
        super.onResume();
        musicAdapter.notifyDataSetChanged(); //刷新列表
    }

    private boolean isServiceBound = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
        //清空列表
        musicList.clear();
        Glide.with(getApplicationContext()).pauseAllRequests();
        saveSettings();

    }
    // 监听组件

    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.player){
                // 进入播放器
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);}
        else if(viewId==R.id.play_or_pause)
                serviceBinder.playOrPause();
        else if(viewId==R.id.playing_list)
                // 显示正在播放列表
                showPlayingList();

    }

    // 初始化活动
    private void initActivity(){
        // 申请读写权限
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{WRITE_EXTERNAL_STORAGE}, 1);

        //绑定控件
        musicListView = findViewById(R.id.music_list);
        RelativeLayout playerToolView = findViewById(R.id.player);
        playingTitleView = findViewById(R.id.playing_title);
        playingArtistView = findViewById(R.id.playing_artist);
        btnPlayOrPause = findViewById(R.id.play_or_pause);
        ImageView btn_playingList = findViewById(R.id.playing_list);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        musicCountView = headerView.findViewById(R.id.nav_num);

        // 设置监听
        playerToolView.setOnClickListener(this);
        btnPlayOrPause.setOnClickListener(this);
        btn_playingList.setOnClickListener(this);

        // 使用ToolBar
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // 设置侧边栏
        setNavigationView();

        // 启动service并绑定
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void initMusicList(){
        //从数据库获取我的音乐
        musicList = new ArrayList<>();
        List<MyMusicDB> list = LitePal.findAll(MyMusicDB.class);
        for (MyMusicDB s:list){
            Music m = new Music(s.music_name);
            musicList.add(m);
        }

        // 音乐列表绑定适配器
        musicAdapter = new MusicAdapter(this, R.layout.music_item, musicList);
        musicListView.setAdapter(musicAdapter);
    }

    // 读取配置
    private void initsettings(){
        spf = getSharedPreferences("settings", MODE_PRIVATE);
    }

    // 保存配置
    private void saveSettings(){
        SharedPreferences.Editor editor = spf.edit();
        editor.apply();
    }

    // 设置侧边栏
    private void setNavigationView() {

        // 使用toggle控制侧边栏弹出:
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);
    }
    // 显示当前正在播放的音乐
    private void showPlayingList(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //设计对话框的显示标题
        builder.setTitle("播放列表");
        //获取播放列表
        final List<Integer> playingList = serviceBinder.getPlayingList();

        if(playingList.size() > 0) {

            //播放列表有曲目，显示所有音乐
            final PlayingMusicAdapter playingAdapter = new PlayingMusicAdapter(this, R.layout.playinglist_item, playingList);
            builder.setAdapter(playingAdapter, new DialogInterface.OnClickListener() {
                //监听列表项点击事件
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            //列表项中删除按钮的点击事件
            playingAdapter.setOnDeleteButtonListener(new PlayingMusicAdapter.onDeleteButtonListener() {
                @Override
                public void onClick(int i) {
                    playingAdapter.notifyDataSetChanged();
                }
            });
        }
        else {
            //播放列表没有曲目，显示没有音乐
            builder.setMessage("没有正在播放的音乐");
        }

        //设置该对话框是可以自动取消的，例如当用户在空白处随便点击一下，对话框就会关闭消失
        builder.setCancelable(true);
        //创建并显示对话框
        builder.create().show();
    }

    // 与后台服务连接的匿名类
    private ServiceConnection serviceConnection = new ServiceConnection() {


        public void onServiceConnected(ComponentName name, IBinder service) {
            //绑定成功后，取得MusicSercice提供的接口
            serviceBinder = (MyService.MyServiceBinder) service;
            // 服务绑定成功时调用
            isServiceBound = true;

            //注册监听器
            serviceBinder.registerOnStateChangeListener(listenr);

            Music item = serviceBinder.getCurrentMusic();

            if(item != null){
                //当前有可播放音乐
                playingTitleView.setText(item.music_name);
            }

        }
        public void onServiceDisconnected(ComponentName name) {
            serviceBinder=null;
            isServiceBound = false;
            //断开连接之后, 注销监听器
            serviceBinder.unregisterOnStateChangeListener(listenr);
        }
    };
    // 实现监听器监听MusicService的变化，
    private MyService.OnStateChangeListenr listenr = new MyService.OnStateChangeListenr() {

        @Override
        public void onPlayProgressChange(long played, long duration) {}

        @Override
        public void onPlay(Music item) {
            //播放状态变为播放时
            btnPlayOrPause.setImageResource(R.drawable.zanting);
            playingTitleView.setText(item.music_name);
        }

        @Override
        public void onPause() {
            //播放状态变为暂停时
            btnPlayOrPause.setImageResource(R.drawable.bofang);
        }
    };
    // 对外接口, 插入一首歌曲
    public static  void addMymusic(Music item){
        if (musicList.contains(item))
            return;
        //添加到列表和数据库
        musicList.add(0, item);
        MyMusicDB myMusic = new MyMusicDB(item.music_name);
        myMusic.save();
    }
    // 显示菜单
    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    }