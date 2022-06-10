package com.example.music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class LocalListsActivity extends AppCompatActivity {

    private List<SongList> songLists = null;
    private SongListAdapter songListAdapter = null;
    private int Icon;

    private LinearLayout ll_songlist_toolbar;
    private ListView list_songlist;
    private LayoutInflater mInflater;
    private ImageView img_song_icon;
    private TextView txt_Songname;
    private TextView txt_singer;
    private ImageView img_pause;

    private CurrentSongApp CSApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_locallists);

        bindView();
//        songs.add(new Song("海阔天空", "Beyond", R.drawable.actionbar_music_normal));
//        songs.add(new Song("白玫瑰", "陈奕迅", R.drawable.actionbar_music_normal));
//        songs.add(new Song("Desperato", "Eagles", R.drawable.actionbar_music_normal));
        mInflater = LocalListsActivity.this.getLayoutInflater();

        songLists = CSApp.getSongLists();
        if (songLists != null) {
            songListAdapter = new SongListAdapter((ArrayList<SongList>) songLists, mInflater);
            list_songlist.setAdapter(songListAdapter);

            list_songlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                }
            });
        }
    }

    private void bindView() {
        list_songlist = (ListView) findViewById(R.id.list_songlist);
        ll_songlist_toolbar = (LinearLayout) findViewById(R.id.ll_songlist_toolbar);
//        img_song_icon = (ImageView) findViewById(R.id.img_song_icon);

        CSApp = (CurrentSongApp) getApplication();
    }
}
