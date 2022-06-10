package com.example.music;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SongList implements Serializable {
    private List<Song> songList;
    private String SongListName;
    private int Icon;

    public SongList(String SongListName, int Icon) {
        this.SongListName = SongListName;
        this.songList = new ArrayList<Song>();
        this.Icon = Icon;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public String getSongListName() {
        return SongListName;
    }

    public int getIcon() {
        return Icon;
    }

    public void setSongListName(String songListName) {
        SongListName = songListName;
    }

    public void addSongList(Song song) {
        songList.add(song);
    }

    public void setIcon(int icon) {
        Icon = icon;
    }
}
