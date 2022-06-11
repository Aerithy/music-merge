package com.example.music;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NetFragment extends Fragment implements View.OnClickListener ,MyViewPager.OnViewPagerTouchListener, ViewPager.OnPageChangeListener{

    private              MyViewPager        mLooperPager;
    private              LooperPagerAdapter mLooperPagerAdapter;
    private Handler mHandler;
    private              boolean            mIsTouch =false;
    private static       List<Integer>      sPics    = new ArrayList<>();


    static{
        sPics.add(R.drawable.first);
        sPics.add(R.drawable.lv1);
        sPics.add(R.drawable.lv2);
        sPics.add(R.drawable.lv3);
        sPics.add(R.drawable.lv4);
    }
    private LinearLayout mPointContainer;

    private String content;
    private View view;
    private LinearLayout ll_song_list_4;
    private LinearLayout ll_song_list_5;
    private LinearLayout ll_song_list_6;
    public NetFragment(String content) {
        this.content = content;
    }

    private SongList songList1;
    private ArrayList<Song> songs_1;
    CurrentSongApp CSApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_net,container,false);
        // txt_content.setText(content);
        bindViews();
        mHandler.post(mLooperTask);
        return view;
    }

    private void bindViews() {
        ll_song_list_4 = (LinearLayout) view.findViewById (R.id.ll_song_list_4);
        ll_song_list_4.setOnClickListener(this);
        ll_song_list_5 = (LinearLayout) view.findViewById (R.id.ll_song_list_5);
        ll_song_list_5.setOnClickListener(this);
        ll_song_list_6 = (LinearLayout) view.findViewById (R.id.ll_song_list_6);
        ll_song_list_6.setOnClickListener(this);
        mPointContainer=(LinearLayout)view.findViewById(R.id.points_container);
        mHandler=new Handler();
        mLooperPager=(MyViewPager) view.findViewById(R.id.looper_pager);
        mLooperPagerAdapter = new LooperPagerAdapter();
        mLooperPagerAdapter.setData(sPics);
        mLooperPager.setAdapter(mLooperPagerAdapter);
        mLooperPager.addOnPageChangeListener(this);
        mLooperPager.setOnViewPagerTouchListener(this);
        insertPoint();
        mLooperPager.setCurrentItem(mLooperPagerAdapter.getDataRealSize()*100,false);

        CSApp = (CurrentSongApp) getActivity().getApplication();
        CSApp.setPlayState(false);
    }
    private void insertPoint() {
        for (int i = 0; i < sPics.size(); i++) {
            View point=new View(this.getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30,30);
            layoutParams.leftMargin=20;
            point.setBackground(getResources().getDrawable(R.drawable.shape_point_normal));
            point.setLayoutParams(layoutParams);
            mPointContainer.addView(point);
        }
    }


    private Runnable mLooperTask = new Runnable() {
        @Override
        public void run() {
            if(!mIsTouch) {
                int currentItem = mLooperPager.getCurrentItem();
                mLooperPager.setCurrentItem(++currentItem, true);

            }
            mHandler.postDelayed(this, 3000);
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int realPosition;
        if(mLooperPagerAdapter.getDataRealSize()!=0){
            realPosition = position%mLooperPagerAdapter.getDataRealSize();
        }else{
            realPosition=0;
        }
        setSelectPoint(realPosition);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPagerTouch(boolean isTouch) {
        mIsTouch=isTouch;
    }
    private void setSelectPoint(int realPosition){

        for (int i = 0; i < mPointContainer.getChildCount(); i++) {
            View point=mPointContainer.getChildAt(i);
            if(i!=realPosition){
                point.setBackgroundResource(R.drawable.shape_point_normal);
            }else {
                point.setBackgroundResource(R.drawable.shape_point_selected);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), SongListActivity.class);
        SongList songList;
        int icon;
        switch(v.getId()) {
            case R.id.ll_song_list_4:
                songList = CSApp.getSongLists().get(0);
                songs_1 = (ArrayList<Song>) songList.getSongList();
                icon = songList.getIcon();
                /* Here You can modify the List object 'songs_1' to determine which and what song
                 * will be shown in the SongListActivity
                 *
                 *
                 * 修改此处代码段来对List对象songs_1进行修改，使其链表内容为下一个歌单Activity中会显示的歌曲 */
//                songs_1.add(new Song("海阔天空", "Beyond", R.drawable.actionbar_music_normal));
//                songs_1.add(new Song("白玫瑰", "陈奕迅", R.drawable.actionbar_music_normal));
//                songs_1.add(new Song("Desperato", "Eagles", R.drawable.actionbar_music_normal));
//                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
                intent.putExtra("song_list", (Serializable) songs_1);
                intent.putExtra("song_icon", icon);
                intent.putExtra("song_intro", songList.getSongListName());
                startActivity(intent);
                break;
            case R.id.ll_song_list_5:
                songList = CSApp.getSongLists().get(1);
                songs_1 = (ArrayList<Song>) songList.getSongList();
                icon = songList.getIcon();
                /* Here You can modify the List object 'songs_1' to determine which and what song
                 * will be shown in the SongListActivity
                 *
                 *
                 * 修改此处代码段来对List对象songs_1进行修改，使其链表内容为下一个歌单Activity中会显示的歌曲 */
//                songs_1.add(new Song("海阔天空", "Beyond", R.drawable.actionbar_music_normal));
//                songs_1.add(new Song("白玫瑰", "陈奕迅", R.drawable.actionbar_music_normal));
//                songs_1.add(new Song("Desperato", "Eagles", R.drawable.actionbar_music_normal));
//                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
                intent.putExtra("song_list", (Serializable) songs_1);
                intent.putExtra("song_icon", icon);
                intent.putExtra("song_intro", songList.getSongListName());
                startActivity(intent);
                break;
            case R.id.ll_song_list_6:
                songList = CSApp.getSongLists().get(2);
                songs_1 = (ArrayList<Song>) songList.getSongList();
                icon = songList.getIcon();
                /* Here You can modify the List object 'songs_1' to determine which and what song
                 * will be shown in the SongListActivity
                 *
                 *
                 * 修改此处代码段来对List对象songs_1进行修改，使其链表内容为下一个歌单Activity中会显示的歌曲 */
//                songs_1.add(new Song("海阔天空", "Beyond", R.drawable.actionbar_music_normal));
//                songs_1.add(new Song("白玫瑰", "陈奕迅", R.drawable.actionbar_music_normal));
//                songs_1.add(new Song("Desperato", "Eagles", R.drawable.actionbar_music_normal));
//                /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
                intent.putExtra("song_list", (Serializable) songs_1);
                intent.putExtra("song_icon", icon);
                intent.putExtra("song_intro", songList.getSongListName());
                startActivity(intent);
                break;
        }
    }
}