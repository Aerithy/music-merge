package com.example.music;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class helpPlaying {
    private List<Song> songList = new ArrayList<>();
    private int position;
    private int songNumber;
    private int playbackOrder=0;
    private int isPlaying=0;
    private Bitmap picture;

    private CurrentSongApp CSApp;


    public void setPicture(Bitmap inPicture) {
        picture=inPicture;
    }//设置当前图片

    public Bitmap getPicture() {
        return picture;
    }//输出当前图片

    public void setIsPlaying(int ifPlaying) {
        this.isPlaying = ifPlaying;
    }//设置播放状态

    public int getIsPlaying() {
        return isPlaying;
    }//输出播放状态

    public int getPlaybackOrder() {
        return playbackOrder;
    }//输出播放顺序

    public void setPlaybackOrder(int playbackOrder) {//设置播放顺序
        this.playbackOrder = playbackOrder;
    }

    public void copyList(List<Song> list) {//初始化
        for (int i = 0; i < list.size()-1; i++) {
            Song song=new Song();
            song.name=list.get(i).name;
            song.path=list.get(i).getPath();
            song.duration=list.get(i).getDuration();
            song.singer=list.get(i).singer;
            song.ifLove=0;
            try
            {songList.add(song);}
            catch (Exception e) {
                System.out.println("try");
                e.printStackTrace();
            }
        }
        songNumber = list.size();
    }

    public Song prev() {//上一首
        if (position == 0) {
            position = songNumber - 1;
        } else {
            position = position - 1;
        }
        return songList.get(position);
    }

    public Song next() {//下一首
        position = (position + 1) % songNumber;
        return songList.get(position);
    }

    public void chooseMusic(int position)
    {
        this.position=position;
    }//选择音乐

    public Song getMusic()
    {
        return songList.get(position);
    }//输出当前音乐

    public Song getRandomMusic()//输出随机音乐
    {
        Random rand = new Random();
        this.position=rand.nextInt(songNumber);
        return songList.get(position);
    }

    public Song oneLoop()//单曲循环
    {
        return getMusic();
    }

    public Song sequential()//顺序播放
    {
        return next();
    }

    public Song randomly()//随机播放
    {
        return getRandomMusic();
    }

    public void setLove()
    {
        songList.get(position).ifLove=1;
    }//设置喜欢

    public void cancelove()
    {
        songList.get(position).ifLove=0;
    }//取消喜欢

    public boolean ifLove()//喜欢状态
    {
        if(songList.get(position).ifLove==0)
            return false;
        else
            return true;
    }

    private static helpPlaying instance = null;//单例

    public static helpPlaying getInstance() {
        if (instance == null) {
            synchronized (helpPlaying.class) {
                if (instance == null) {//2
                    instance = new helpPlaying();
                }
            }
        }
        return instance;
    }


}
