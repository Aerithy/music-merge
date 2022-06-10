package com.example.music;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class CurrentSongApp extends Application {
    private Song CurrentSong;
    private Song NextSong;
    private boolean PlayState;
    private List<Song> CurrentPlayList;
    private List<SongList> songLists;
    private List<Song> localsongs;

    public Song getCurrentSong () {
        return CurrentSong;
    }

    public Song getNextSong () {
        return NextSong;
    }

    public boolean getPlayState () {
        return PlayState;
    }

    public List<Song> getCurrentPlayList() {
        return CurrentPlayList;
    }

    public List<Song> getLocalsongs() { return localsongs; }

    public List<SongList> getSongLists() { return songLists; }

    public void setCurrentSong (Song currentSong) {
        this.CurrentSong = currentSong;
    }

    public void setNextSong (Song nextSong) {
        this.NextSong = nextSong;
    }

    public void setPlayState (boolean playState) {
        this.PlayState = playState;
    }

    public void setCurrentPlayList (List<Song> currentPlayList) {
        this.CurrentPlayList = currentPlayList;
    }

    public void setSongLists (List<SongList> songLists) {
        this.songLists = songLists;
    }

    public void setLocalsongs (List<Song> localsongs) {
        this.localsongs = localsongs;
    }

    public void addSongList (SongList songList) { this.songLists.add(songList); }

    public void delSongList (int position) { this.songLists.remove(position); }

    public void addSong (Song song) { this.localsongs.add(song); }

    public void delSong (int position) { this.localsongs.remove(position); }

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/huawenxingkai.ttf");
        FontsOverride.setDefaultFont(this, "SANS", "fonts/huawenxingkai.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/huawenxingkai.ttf");
    }
}
