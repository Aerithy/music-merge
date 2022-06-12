package com.example.music;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LocalSongsActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Song> songs = null;
    private SongAdapter songAdapter = null;
    private int Icon;

    private LinearLayout ll_songlist_toolbar;
    private LayoutInflater mInflater;
    private ImageView img_song_icon;
    private TextView txt_title;
    private TextView txt_Songname;
    private TextView txt_singer;
    private ImageView img_pause;

    private CurrentSongApp CSApp;

    private List<Song> songList=new ArrayList<Song>();
    LinearLayoutManager layoutManager;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_localsongs);

        bindView();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("Act_type");
        if (type == 1) {
            txt_title.setText("我喜欢");
        }
        loadSongList();
//        songs.add(new Song("海阔天空", "Beyond", R.drawable.actionbar_music_normal));
//        songs.add(new Song("白玫瑰", "陈奕迅", R.drawable.actionbar_music_normal));
//        songs.add(new Song("Desperato", "Eagles", R.drawable.actionbar_music_normal));
//        mInflater = LocalSongsActivity.this.getLayoutInflater();
//        songs = CSApp.getLocalsongs();
//        if (songs != null) {
//            songAdapter = new SongAdapter((ArrayList<Song>) songs, mInflater);
//            list_song.setAdapter(songAdapter);
//
//            list_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Song current_play = songs.get(position);
//                    /*Set the playing song field here
//                     * Code:
//                     * */
//                    CSApp.setCurrentSong(current_play);
//                    txt_Songname.setText(CSApp.getCurrentSong().getSongName());
//                    txt_singer.setText(CSApp.getCurrentSong().getSinger());
//                    CSApp.setPlayState(true);
//                    img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
//                }
//            });
//        }
//
//        if (!CSApp.getPlayState()) {
//            img_pause.setBackgroundResource(R.drawable.playbar_btn_pause);
//        }
//        else {
//            img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
//        }
//
//        if (CSApp.getCurrentSong() != null) {
//            txt_Songname.setText(CSApp.getCurrentSong().getSongName());
//            txt_singer.setText(CSApp.getCurrentSong().getSinger());
//        }
    }

    private void bindView() {
        ll_songlist_toolbar = (LinearLayout) findViewById(R.id.ll_songlist_toolbar);
        txt_Songname = (TextView) findViewById(R.id.txt_Songname);
        txt_singer = (TextView) findViewById(R.id.txt_singer);
        txt_title = (TextView) findViewById(R.id.txt_title);

        img_pause = (ImageView) findViewById(R.id.img_pause);

        img_pause.setOnClickListener(this);

        CSApp = (CurrentSongApp) getApplication();
    }
    public void loadSongList() {
        songList = CSApp.getLocalsongs();//初始化获取歌曲信息
        if (type == 1) {
            List<Song> temp = new ArrayList<Song>();
            for (Song song:songList) {
                if (song.ifLove == 1) {
                    temp.add(song);
                }
            }
            songList = temp;
        }
        SongAdapter songAdapter = new SongAdapter(songList);//歌曲适配器
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        layoutManager = new LinearLayoutManager(LocalSongsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(songAdapter);//加载歌曲列表

        //设置音乐点击事件
        songAdapter.setOnSongItemClickListen(new SongAdapter.OnSongItemClickListen() {
            @Override
            public void onClick_Song(int position) {
                Toast.makeText(LocalSongsActivity.this,"position is"+position,Toast.LENGTH_SHORT).show();
                CSApp.setCurrentSong(songList.get(position));
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(LocalSongsActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pause:
                if (CSApp.getPlayState()) {
                    CSApp.setPlayState(false);
                    img_pause.setBackgroundResource(R.drawable.playbar_btn_pause);
                }
                else {
                    CSApp.setPlayState(true);
                    img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
                }
                // showAllFragment(fTransaction);
                break;
        }
    }
    @Override
    public void onResume() {

        super.onResume();
        if (!CSApp.getPlayState()) {
            img_pause.setBackgroundResource(R.drawable.playbar_btn_pause);
        }
        else {
            img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
        }

        if (CSApp.getCurrentSong() != null) {
            txt_Songname.setText(CSApp.getCurrentSong().getName());
            txt_singer.setText(CSApp.getCurrentSong().getSinger());
            ImageView nowSong=findViewById(R.id.img_playlist);
            nowSong.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(LocalSongsActivity.this, PlayActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
