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
import java.util.LinkedList;
import java.util.List;

public class SongListActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Song> songs = null;
    private SongAdapter songAdapter = null;
    private int Icon;
    private String Intro;

    private LinearLayout ll_songlist_toolbar;
    private LayoutInflater mInflater;
    private ImageView img_songlist_icon;
    private TextView txt_songlist_intro;
    private ImageView img_pause;
    private TextView txt_Songname;
    private TextView txt_singer;

    private CurrentSongApp CSApp;

    private List<Song> songList=new ArrayList<Song>();
    LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_songlist);
        bindView();

        int pos = getIntent().getIntExtra("song_list_pos", 0);
        songs = CSApp.getSongLists().get(pos).getSongList();
        // songs = (List<Song>) getIntent().getSerializableExtra("song_list");
        Icon = getIntent().getIntExtra("song_icon", R.drawable.actionbar_music_normal);
        Intro = getIntent().getStringExtra("song_intro");
        txt_songlist_intro.setText(Intro);
        img_songlist_icon.setBackgroundResource(Icon);

        loadSongList();
//        songs.add(new Song("海阔天空", "Beyond", R.drawable.actionbar_music_normal));
//        songs.add(new Song("白玫瑰", "陈奕迅", R.drawable.actionbar_music_normal));
//        songs.add(new Song("Desperato", "Eagles", R.drawable.actionbar_music_normal));
//        mInflater = SongListActivity.this.getLayoutInflater();
//        songAdapter = new SongAdapter((ArrayList<Song>) songs, mInflater);
//        list_song.setAdapter(songAdapter);

//        list_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Song current_play = songs.get(position);
//                /*Set the playing song field here
//                * Code:
//                * */
//                CSApp.setCurrentSong(current_play);
////                txt_Songname.setText(CSApp.getCurrentSong().getSongName());
//                txt_singer.setText(CSApp.getCurrentSong().getSinger());
//                CSApp.setPlayState(true);
//                img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
//            }
//        });
//
//        if (!CSApp.getPlayState()) {
//            img_pause.setBackgroundResource(R.drawable.playbar_btn_pause);
//        }
//        else {
//            img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
//        }
//
//        if (CSApp.getCurrentSong() != null) {
////            txt_Songname.setText(CSApp.getCurrentSong().getSongName());
//            txt_singer.setText(CSApp.getCurrentSong().getSinger());
//        }
//
//        int i = ll_songlist_toolbar.getDrawingCacheBackgroundColor();
//
//        img_songlist_icon.setBackgroundResource(Icon);
    }

    private void bindView() {
        ll_songlist_toolbar = (LinearLayout) findViewById(R.id.ll_songlist_toolbar);
        img_songlist_icon = (ImageView) findViewById(R.id.img_songlist_icon);
        txt_songlist_intro = (TextView) findViewById(R.id.txt_songlist_intro);
        txt_Songname = (TextView) findViewById(R.id.txt_Songname);
        txt_singer = (TextView) findViewById(R.id.txt_singer);

        img_pause = (ImageView) findViewById(R.id.img_pause);

        img_pause.setOnClickListener(this);

        CSApp = (CurrentSongApp) getApplication();
    }

    public void loadSongList() {
        /*此处特别修改，根据上一个页面传值决定其歌曲列表*/
//        songList = CSApp.getLocalsongs();//初始化获取歌曲信息
        songList = songs;
        /***************************************/
        SongAdapter songAdapter = new SongAdapter(songList);//歌曲适配器
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        layoutManager = new LinearLayoutManager(SongListActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(songAdapter);//加载歌曲列表

        //设置音乐点击事件
        songAdapter.setOnSongItemClickListen(new SongAdapter.OnSongItemClickListen() {
            @Override
            public void onClick_Song(int position) {
                Toast.makeText(SongListActivity.this,"position is"+position,Toast.LENGTH_SHORT).show();
                CSApp.setCurrentSong(songList.get(position));
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(SongListActivity.this, PlayActivity.class);
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
}
