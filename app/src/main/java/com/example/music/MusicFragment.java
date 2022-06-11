package com.example.music;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MusicFragment extends Fragment implements View.OnClickListener{

    private String content;
    private View view;
    private List<SongList> songLists = null;
    private SongListAdapter songListAdapter = null;
    private ListView list_songlist;
    private LayoutInflater mInflater;

    private Button btn_local_music;
    private Button btn_recently;
    private Button btn_download_manage;
    private Button btn_my_singer;

    private CurrentSongApp CSApp;

    public MusicFragment(String content) {

        this.content = content;

    }

    private void bindViews() {
        btn_local_music = (Button) view.findViewById(R.id.btn_local_music);
        btn_recently = (Button) view.findViewById(R.id.btn_recently);
        btn_download_manage = (Button) view.findViewById(R.id.btn_download_manage);
        btn_my_singer = (Button) view.findViewById(R.id.btn_my_singer);

        btn_local_music.setOnClickListener(this);
        btn_recently.setOnClickListener(this);
        btn_download_manage.setOnClickListener(this);
        btn_my_singer.setOnClickListener(this);

        CSApp = (CurrentSongApp) getActivity().getApplication();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        songLists = new ArrayList<>();

    }

    public void initSongLists() {
        songLists = new LinkedList<SongList>();
        // Usually the variable songList's content should be read from the storage
        SongList songList = new SongList(content, R.drawable.actionbar_music_normal);
    }

    public void addSongList(String SongListName, int Icon) {
        SongList songList = new SongList(SongListName, Icon);
//        songList.addSongList(new Song("Song 1", "singer 1", R.drawable.actionbar_music_normal));
//        songList.addSongList(new Song("Song 2", "singer 2", R.drawable.actionbar_music_normal));
        songLists.add(songList);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_music,container,false);
        TextView txt_content = (TextView) view.findViewById(R.id.txt_songlist);
        list_songlist = (ListView) view.findViewById(R.id.list_songlist);
        bindViews();

        songLists = CSApp.getSongLists();
        mInflater = getActivity().getLayoutInflater();
        songListAdapter = new SongListAdapter((ArrayList<SongList>) songLists, mInflater);
        list_songlist.setAdapter(songListAdapter);

        list_songlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), SongListActivity.class);
                intent.putExtra("song_list", (Serializable) songLists.get(position).getSongList()); // Send the song list content(A list of songs)
                intent.putExtra("song_icon", songLists.get(position).getIcon());    // Send the song list icon
                intent.putExtra("song_intro", songLists.get(position).getSongListName());
                startActivity(intent);
            }
        });

        txt_content.setText(content);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_local_music:
                Intent intent = new Intent(getActivity(), LocalSongsActivity.class);
                startActivity(intent);
                break;
        }
    }
}
