package com.example.music;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.navigation.NavigationView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{

    private LinearLayout toolbar;
    private ImageView bar_net;
    private ImageView bar_music;
    private ImageView bar_menu;
    private ImageView bar_friends;
    private ImageView bar_search;

    private LinearLayout ll_tab_bar;
    private ImageView img_playlist;
    private TextView txt_Songname;
    private TextView txt_singer;
    private ImageView img_list;
    private ImageView img_pause;
    private ImageView img_next;
    private FrameLayout ly_content;

    private NetFragment fg1;
    private MusicFragment fg2;
    private FriendFragment fg3;
    private FragmentManager fm;

    private CurrentSongApp CSApp;
    private boolean FgArr;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private static final int CHOOSE_HEAD = 1;
    private static final int CHOOSE_PHOTO = 2;

    private void bindViews() {


        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.nav_view) ;

        bar_menu = (ImageView) findViewById(R.id.bar_menu);
        bar_net = (ImageView) findViewById(R.id.bar_net);
        bar_music = (ImageView) findViewById(R.id.bar_music);
        bar_friends = (ImageView) findViewById(R.id.bar_friends);
        bar_search=(ImageView)findViewById(R.id.bar_search);
        bar_search.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        ll_tab_bar = (LinearLayout) findViewById(R.id.ll_tab_bar);
        img_playlist = (ImageView) findViewById(R.id.img_playlist);
        txt_Songname = (TextView) findViewById(R.id.txt_Songname);
        txt_singer = (TextView) findViewById(R.id.txt_singer);
        img_list = (ImageView) findViewById(R.id.img_list);
        img_pause = (ImageView) findViewById(R.id.img_pause);
        img_next = (ImageView) findViewById(R.id.img_next);
        toolbar = (LinearLayout) findViewById(R.id.toolbar_menu);
        ly_content = (FrameLayout) findViewById(R.id.ly_content);

        bar_menu.setOnClickListener(this);
        bar_net.setOnClickListener(this);
        bar_music.setOnClickListener(this);
        bar_friends.setOnClickListener(this);
        img_pause.setOnClickListener(this);

        CSApp = (CurrentSongApp) getApplication();
        CSApp.setPlayState(false);


    }


    private void setSelected() {
        bar_net.setSelected(false);
        bar_music.setSelected(false);
        bar_friends.setSelected(false);
    }

    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fg1 != null)fragmentTransaction.hide(fg1);
        if(fg2 != null)fragmentTransaction.hide(fg2);
        if(fg3 != null)fragmentTransaction.hide(fg3);
    }

    private void initLocalSongs() {
        List<Song> songs = new ArrayList<Song>();
        songs=ScanMusic.getMusicData(MainActivity.this);
        CSApp.setLocalsongs(songs);
    }

    private void initSongLists() {
        ArrayList<SongList> songLists = new ArrayList<SongList>();

        CSApp.setSongLists(songLists);
        ArrayList<Song> localsongs = (ArrayList<Song>) CSApp.getLocalsongs();
        SongList songList_1 = new SongList("Song List 4", R.drawable.gargantua);
        SongList songList_2 = new SongList("Song List 4", R.drawable.electric_state);
        SongList songList_3 = new SongList("Song List 4", R.drawable.space_girl);

        Random rd = new Random();
        for(Song song:localsongs) {
            int temp = rd.nextInt(100);
            if (temp < 33) {
                songList_1.addSongList(song);
            }
            else if (temp < 66) {
                songList_2.addSongList(song);
            }
            else {
                songList_3.addSongList(song);
            }
        }

        CSApp.addSongList(songList_1);
        CSApp.addSongList(songList_2);
        CSApp.addSongList(songList_3);

    }
    private void initNavMenu()
        {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                       @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.changeBg:
                        Toast.makeText(MainActivity.this,"更改背景图",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("image/*");
                        startActivityForResult(intent,CHOOSE_PHOTO);//打开相册
                        break;
                    case R.id.change_head:
                        Toast.makeText(MainActivity.this,"更改头像",Toast.LENGTH_SHORT).show();
                        Intent head_intent=new Intent("android.intent.action.GET_CONTENT");
                        head_intent.setType("image/*");
                        startActivityForResult(head_intent,CHOOSE_HEAD);//打开相册
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }


        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if(resultCode==RESULT_OK){
                    //判断手机版本号
                    if(Build.VERSION.SDK_INT>=19){
                        //api 19为安卓4.4以上用这个方法处理图片
                        handleImageOnKitKat(data,CHOOSE_PHOTO);
                    }
                }
                break;
            case CHOOSE_HEAD:
                if(resultCode==RESULT_OK){
                    //判断手机版本号
                    if(Build.VERSION.SDK_INT>=19){
                        //api 19为安卓4.4以上用这个方法处理图片
                        handleImageOnKitKat(data,CHOOSE_HEAD);
                    }
                }
                break;
            default:
                break;
        }
    }
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data,int CODE) {
        String imagePath=null;
        Uri uri=data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            //如果是doucument类型的Uri，则通过document id处理
            String docId=DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id=docId.split(":")[1];//解析出数字格式的id
                String selection= MediaStore.Images.Media._ID+"="+id;
                imagePath=getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri= ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(docId));
                imagePath=getImagePath(contentUri,null);
            }
            Log.d("uri类型：", "handleImageOnKitKat: content类型的Uri");
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            //如果是content类型的Uri，则使用普通方式处理
            imagePath=getImagePath(uri,null);
            Log.d("uri类型：", "handleImageOnKitKat: content类型的Uri");
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath=uri.getPath();
            Log.d("uri类型：", "handleImageOnKitKat: file类型的Uri");
        }
        disPlayImage(imagePath,CODE);
    }
    @SuppressLint("Range")
    private String getImagePath(Uri uri, String selection) {
        String path=null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor=getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToNext()){
                path=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void disPlayImage(String imagePath,int CODE) {
        if(imagePath!=null){
            switch (CODE){
                case CHOOSE_PHOTO:
                    Bitmap bitmap_1= BitmapFactory.decodeFile(imagePath);
                    BitmapDrawable bd_1=new BitmapDrawable(bitmap_1);
                    RelativeLayout relativeLayout_1=findViewById(R.id.brelativelayout);
                    relativeLayout_1.setBackground(bd_1);
                    break;
                case CHOOSE_HEAD:
                    Bitmap bitmap=BitmapFactory.decodeFile(imagePath);
                    ImageView head_2=findViewById(R.id.headImage);
                    head_2.setImageBitmap(bitmap);
                    break;
                default:
                    break;
            }

        }else{
            Toast.makeText(this,"设置背景图失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fm = getSupportFragmentManager();
        bindViews();
        initNavMenu();
        initLocalSongs();
        initSongLists();
        bar_net.performClick();
        checkUserLogin();
//
//        if (!CSApp.getPlayState()) {
//            img_pause.setBackgroundResource(R.drawable.playbar_btn_pause);
//        }
//        else {
//            img_pause.setBackgroundResource(R.drawable.playbar_btn_play);
//        }
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fm.beginTransaction();

        switch (v.getId()) {
            case R.id.bar_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.bar_net:
                hideAllFragment(fTransaction);
                setSelected();
                bar_net.setSelected(true);
                if (fg1 == null) {
                    fg1 = new NetFragment("First Fragment");
                    fTransaction.add(R.id.ly_content, fg1);
                }
                else {
                    fTransaction.show(fg1);
                }
                break;
            case R.id.bar_music:
                hideAllFragment(fTransaction);
                setSelected();
                bar_music.setSelected(true);
                if (fg2 == null) {
                    fg2 = new MusicFragment("Song Lists");
                    fTransaction.add(R.id.ly_content, fg2);
                }
                else {
                    fTransaction.show(fg2);
                }
                break;
            case R.id.bar_friends:
                hideAllFragment(fTransaction);
                setSelected();
                bar_friends.setSelected(true);
                if (fg3 == null) {
                    fg3 = new FriendFragment("Third Fragment");
                    fTransaction.add(R.id.ly_content, fg3);
                }
                else {
                    fTransaction.show(fg3);
                }
                break;
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
        fTransaction.commit();
    }
    private void checkUserLogin(){
        //查询有无登录记录
        List<User> findUser= LitePal.where("userStatus=?","1")
                .find(User.class);
        //有用户显示用户名，没用户显示登录
        TextView username=(TextView)navigationView.getHeaderView(0).findViewById(R.id.name);

        //如果无用户登陆过
        if(findUser.size()==0){
            username.setText("请登录");
            //进入登陆页面
            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
        }

        //当前有登录用户
        if(findUser.size()>0)
        {
            User nowUser=findUser.get(0);
            //显示用户名
            username.setText(nowUser.getUserName()+'\n'+nowUser.getUserNumber());

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
                    intent.setClass(MainActivity.this, PlayActivity.class);
                    startActivity(intent);
                }
            });
        }

    }
}
