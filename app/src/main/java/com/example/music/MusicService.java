package com.example.music;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import java.io.IOException;

public class MusicService extends Service {
    public static final String PLAY = "play";
    public static final String PAUSE = "pause";
    public static final String PREV = "prev";
    public static final String NEXT = "next";
    public static final String LOVE = "love";
    private RemoteViews remoteViews;
    private NotificationManager manager;
    private Notification notificaton;
    private Bitmap bitmap;
    private Song song;
    public static MediaPlayer mp = new MediaPlayer();
    private Handler handler = new Handler();
    helpPlaying hp=helpPlaying.getInstance();
    public MusicService() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//初始化并播放音乐
        if(hp.getIsPlaying()==1||hp.getIsPlaying()==2)
        {
            mp.reset();
            hp.setIsPlaying(0);
        }
        song=hp.getMusic();

        try {
            mp.setDataSource(song.path);//读取sd卡
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        mp.setLooping(false);
        initNotification();
        changeBroad();
        mp.start();
        hp.setIsPlaying(1);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//监听器，判断当前音乐是否播放完毕
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                hp.setIsPlaying(0);
                System.out.println("下一首");
                switch(hp.getPlaybackOrder()){//判断播放顺序
                    case 0:{
                        song=hp.oneLoop();
                        break;
                    }
                    case 1:{
                        song=hp.sequential();
                        break;
                    }
                    case 2:{
                        song=hp.randomly();
                        break;
                    }
                }
                try {
                    mp.setDataSource(song.path);//读取sd卡
                    mp.prepare();
                    mp.setLooping(false);
                    mp.start();
                    hp.setIsPlaying(1);
                    changeBroad();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("输出内容");
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotification()//生成通知栏
    {
        //通道设置
        String id = "my_channel_01";
        CharSequence name = "channel";
        String description = "description";
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        //自定义通知栏设置
        remoteViews = new RemoteViews(getPackageName(), R.layout.notifation);
        remoteViews.setTextViewText(R.id.mn,song.name);
        remoteViews.setTextViewText(R.id.sn,song.singer);
        remoteViews.setImageViewBitmap(R.id.notificationImage,hp.getPicture());
        Intent intent = new Intent(this,MainActivity.class);
//        PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pi;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pi = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pi = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Intent intent1 = new Intent(PLAY);
//        PendingIntent pi1 = PendingIntent.getBroadcast(this,0,intent1,0);
        PendingIntent pi1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pi1 = PendingIntent.getActivity(this, 123, intent1, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pi1 = PendingIntent.getActivity(this, 123, intent1, PendingIntent.FLAG_ONE_SHOT);
        }
        remoteViews.setOnClickPendingIntent(R.id.button1,pi1);
        Intent intent3 = new Intent(NEXT);
//        PendingIntent pi3 = PendingIntent.getBroadcast(this,0,intent3,0);
        PendingIntent pi3;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pi3 = PendingIntent.getActivity(this, 123, intent3, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pi3 = PendingIntent.getActivity(this, 123, intent3, PendingIntent.FLAG_ONE_SHOT);
        }
        remoteViews.setOnClickPendingIntent(R.id.button3,pi3);

        //接收广播，用于实现通知栏与服务交互功能
        MusicReceiver musicReceiver = new MusicReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(PLAY);
        intentFilter.addAction(NEXT);
        registerReceiver(musicReceiver, intentFilter);

        notificaton = new Notification.Builder(this, id)
                .setAutoCancel(false)
                .setContentTitle("title")
                .setContentText("text")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setOngoing(true)
                .setCustomContentView(remoteViews)
                .setWhen(System.currentTimeMillis())
                .setOnlyAlertOnce(true)
                .build();
        manager.notify(1, notificaton);
    }
    public class MusicReceiver extends BroadcastReceiver {
        public static final String TAG = "MusicReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
             * */
            //UI控制
            System.out.println("onReceive");
            Log.d("receiver", intent.getAction());
            switch (intent.getAction()) {
                case PLAY: {
                    if (hp.getIsPlaying() == 0) {
                        mp.start();
                        hp.setIsPlaying(1);
                        break;
                    }else if(hp.getIsPlaying() == 2)
                    {
                        mp.start();
                        hp.setIsPlaying(1);
                        break;
                    }
                    else {
                        mp.pause();
                        hp.setIsPlaying(2);
                        break;
                    }
                }
                case NEXT: {
                    mp.reset();
                    hp.setIsPlaying(0);
                    manager.notify(1, notificaton);
                    song = hp.next();
                    try {
                        String name = song.name;
                        mp.setDataSource(song.path);//读取sd卡
                        mp.prepare();
                        mp.setLooping(false);
                        mp.start();
                        hp.setIsPlaying(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("输出内容");
                    }
                    break;
                }
            }

        }

    }

    @Override
    public void onDestroy() {
        manager.cancel(1);
        mp.reset();
        hp.setIsPlaying(0);
        super.onDestroy();
    }

    private IBinder mBinder = new MyBinder();
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public class MyBinder extends Binder
    {

        @RequiresApi(api = Build.VERSION_CODES.Q)
        @SuppressLint("ResourceType")
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

            switch (code) {
                case 101://“还未播放”状态，点击开始播放
                    mp.start();
                    hp.setIsPlaying(1);
                    break;
                case 102://“正在播放”状态，点击暂停
                    mp.pause();
                    hp.setIsPlaying(2);
                    break;
                case 103://“正在暂停”状态，点击继续播放
                    mp.start();
                    hp.setIsPlaying(1);
                    break;
                case 0://设置单曲循环
                    hp.setPlaybackOrder(0);
                    System.out.println("单曲循环");
                    break;
                case 1://设置顺序播放
                    hp.setPlaybackOrder(1);
                    System.out.println("顺序播放");
                    break;
                case 2://设置随机播放
                    hp.setPlaybackOrder(2);
                    System.out.println("随机播放");
                    break;
                case 3://上一首
                    mp.reset();
                    hp.setIsPlaying(0);
                    song=hp.prev();
                    try {
                        String name=song.name;
                        mp.setDataSource(song.path);//读取sd卡
                        mp.prepare();
                        mp.setLooping(false);
                        mp.start();
                        hp.setIsPlaying(1);
                        changeBroad();
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("输出内容");
                    }
                    break;
                case 4://下一首
                    mp.reset();
                    hp.setIsPlaying(0);
                    song=hp.next();
                    try {
                        String name=song.name;
                        mp.setDataSource(song.path);//读取sd卡
                        mp.prepare();
                        mp.setLooping(false);
                        mp.start();
                        hp.setIsPlaying(1);
                        changeBroad();

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("输出内容");
                    }
                    break;
                case 5://修改通知栏歌曲信息
                    remoteViews.setTextViewText(R.id.mn,song.name);
                    remoteViews.setTextViewText(R.id.sn,song.singer);
                    System.out.println("setTextViewText");
                    remoteViews.setImageViewBitmap(R.id.notificationImage,hp.getPicture());
                    manager.notify(1, notificaton);
                    break;
                case 6://修改通知栏播放键
                    if(hp.getIsPlaying()==1)
                    {
                        remoteViews.setImageViewResource(R.id.button1,R.mipmap.playbar_btn_pause);
                    }
                    else
                    {
                        remoteViews.setImageViewResource(R.id.button1,R.mipmap.playbar_btn_play);
                    }
                    manager.notify(1, notificaton);
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }

    }

    public void changeBroad()//音乐变更广播
    {
        Intent intent=new Intent("change");
        sendBroadcast(intent);
    }
}