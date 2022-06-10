package com.example.music;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

public class PlayActivity extends AppCompatActivity {

    private android.widget.ImageView ImageView;//UI控件
    private Button play;
    private Button prev;
    private Button next;
    private Button love;
    private Button playbackOrder;
    private Button exit;
    private TextView progressTime;
    private TextView totalTime;
    private TextView musicName;
    private TextView artist;
    private SeekBar seekBar;
    private ObjectAnimator objectAnimator;//图片的动画效果

    private int position;//歌曲索引
    private String title="";//歌曲名字
    private String artistName="";//歌手名字
    private String path="";//歌曲路径
    private SimpleDateFormat t = new SimpleDateFormat("mm:ss");//时间格式
    private String time="";//歌曲时长
    public Button love1;

    private Handler handler = new Handler();
    private IBinder mBinder;
    private ServiceConnection mConnection;
    private int isPlaying=-1;
    helpPlaying hp=helpPlaying.getInstance();
    private int rotationFlag=0;

    //针对sd卡读取权限申请
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        List<Song> List = ScanMusic.getMusicData(PlayActivity.this);
        hp.copyList(List);//新增
        Bundle bundle = getIntent().getExtras();
        position = bundle.getInt("position");
        hp.chooseMusic(position);

        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(hp.getMusic().path);
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            //获取bitmap对象
            hp.setPicture(BitmapFactory.decodeByteArray(data, 0, data.length));

        } else {
            //从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
            hp.setPicture(((BitmapDrawable)getResources().getDrawable(R.mipmap.image)).getBitmap());
        }

        service_connection();

        //申请sd卡读取权限
        verifyStoragePermissions(PlayActivity.this);
        //初始化界面并设置点击事件
        findView();
        initView();
        myClick();

        //利用handler实时更新UI界面：时间和进度条
        update();


    }

    public void service_connection() {
        mConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service)
            {
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
            }
        };
        Intent intent = new Intent(this, com.example.music.MusicService.class);
        startService(intent);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // 检查是否有手机sd卡读取权限
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 如果没有权限，则申请，如下
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String timeParse(long duration) {//时间格式转化
        String time = "" ;
        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;
        long second = Math.round((float)seconds/1000) ;
        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;
        if( second < 10 ){
            time += "0" ;
        }
        time += second ;
        return time ;
    }
    public void update()
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.obtainMessage(123).sendToTarget();

                }
            }
        }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 123:
                        //UI更新
                        progressTime.setText(t.format(com.example.music.MusicService.mp.getCurrentPosition()));
                        seekBar.setProgress(com.example.music.MusicService.mp.getCurrentPosition());
                        if(hp.getIsPlaying()!=1)
                        {
                            play.setBackgroundResource(R.mipmap.playbar_btn_play);
                            if(rotationFlag==1)
                            {
                                objectAnimator.pause();
                                rotationFlag=2;
                            }

                        }
                        else
                        {
                            play.setBackgroundResource(R.mipmap.playbar_btn_pause);

                            if(rotationFlag==0)
                            {
                                objectAnimator.start();
                                rotationFlag=1;
                            }
                            else if(rotationFlag==2)
                            {
                                objectAnimator.resume();
                                rotationFlag=1;
                            }
                        }
                        if(hp.ifLove())
                        {
                            love.setBackgroundResource(R.mipmap.play_icn_loved);
                        }
                        else
                        {
                            love.setBackgroundResource(R.mipmap.play_icn_love_prs);
                        }
                        try {
                            int code = 6;
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            mBinder.transact(code, data, reply, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        break;
                    default:
                        break;
                }
            }
        };
    }
    private void findView()
    {
        ImageView = (android.widget.ImageView) findViewById(R.id.img);
        play = (Button) findViewById(R.id.playButton);
        playbackOrder = (Button) findViewById(R.id.playbackOrder);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
        love = (Button) findViewById(R.id.love);
        exit = (Button) findViewById(R.id.exit);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        progressTime = (TextView) findViewById(R.id.progressTime);
        totalTime = (TextView) findViewById(R.id.totalTime);
        musicName=(TextView)findViewById(R.id.musicName);
        artist=(TextView)findViewById(R.id.singer);
        objectAnimator = ObjectAnimator.ofFloat(ImageView, "rotation", 0f, 360f);
        objectAnimator.setDuration(10000);//设置动画时长,10000表示慢慢旋转
        objectAnimator.setInterpolator(new LinearInterpolator());//匀速
        objectAnimator.setRepeatCount(-1);//不停止
        seekBar.setEnabled(true);
        seekBar.setProgress(0);
        seekBar.setMax(250000);
    }
    public class changeReceiver extends BroadcastReceiver {//接受音乐变更广播
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * */
            //UI控制
            System.out.println("changeReceiveronReceive");
            Log.d("receiver", intent.getAction());
            title=hp.getMusic().getName();
            artistName=hp.getMusic().getSinger();
            time=timeParse(hp.getMusic().duration);
            totalTime.setText(time);
            musicName.setText(title);
            artist.setText(artistName);
            ImageView.setImageBitmap(getAlbumPicture(hp.getMusic().path));
            try {
                int code = 5;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                mBinder.transact(code, data, reply, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void initView() {
        play.setTag(0);//Tag:0未开始，1表示正在运行，2表示已被暂停
        playbackOrder.setTag(0);
        love.setTag(0);
        changeReceiver changeReceiver = new changeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("change");
        registerReceiver(changeReceiver, intentFilter);

    }

    public void myClick() {
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hp.getIsPlaying()==0) {
                    play.setTag(1);
                    try {
                        int code = 101;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (hp.getIsPlaying()==1) {
                    try {
                        int code = 102;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if(hp.getIsPlaying()==2){
                    try {
                        int code = 103;
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        mBinder.transact(code, data, reply, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        playbackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(playbackOrder.getTag()==null)
                {
                    System.out.println("NULL");
                }
                else
                {
                    int tag = (Integer) playbackOrder.getTag();
                    if(tag==0)
                    {
                        playbackOrder.setTag(1);
                        try {
                            int code = 1;
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            playbackOrder.setBackgroundResource(R.mipmap.play_icn_loop);
                            mBinder.transact(code, data, reply, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(tag==1)
                    {
                        playbackOrder.setTag(2);
                        try {
                            int code = 2;
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            playbackOrder.setBackgroundResource(R.mipmap.play_icn_shuffle);
                            mBinder.transact(code, data, reply, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(tag==2)
                    {
                        playbackOrder.setTag(0);
                        try {
                            int code = 0;
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            playbackOrder.setBackgroundResource(R.mipmap.play_icn_one);
                            mBinder.transact(code, data, reply, 0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        System.out.println("出错");
                    }
                }



            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int code = 3;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int code = 4;
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(code, data, reply, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = (Integer) love.getTag();
                if (!hp.ifLove())
                {
                    love.setTag(1);
                    hp.setLove();
                    Toast.makeText(PlayActivity.this,"添加喜欢",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    love.setTag(0);
                    hp.cancelove();
                    Toast.makeText(PlayActivity.this,"取消喜欢",Toast.LENGTH_SHORT).show();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //拖动进度条，改变音乐播放进度
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    com.example.music.MusicService.mp.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //用户同意权限
        } else {
            //用户拒绝权限
            System.exit(0);
        }
    }

    //@Override
    //public void onBackPressed() {
    //moveTaskToBack(true);//按下back键后仍能后台运行
    //}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
        mConnection = null;
        try {
            PlayActivity.this.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getAlbumPicture(String path) {//获取歌曲图片
        //歌曲检索
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        //设置数据源
        mmr.setDataSource(path);
        //获取图片数据
        byte[] data = mmr.getEmbeddedPicture();
        Bitmap albumPicture = null;
        if (data != null) {
            //获取bitmap对象
            hp.setPicture(BitmapFactory.decodeByteArray(data, 0, data.length));
            albumPicture = createCircleImage(BitmapFactory.decodeByteArray(data, 0, data.length));

        } else {
            //从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
            hp.setPicture(((BitmapDrawable)getResources().getDrawable(R.mipmap.image)).getBitmap());
            albumPicture = createCircleImage(((BitmapDrawable)getResources().getDrawable(R.mipmap.image)).getBitmap());

        }
        return albumPicture;
    }

    public static Bitmap createCircleImage(Bitmap source) {//图片圆形裁剪
        int length = source.getWidth() < source.getHeight() ? source.getWidth() : source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap target = Bitmap.createBitmap(length, length, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        canvas.drawCircle(length / 2, length / 2, length / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        return target;
    }



}