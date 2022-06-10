package com.example.music;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/5/21 0021.
 */
public class MyOpenHelper extends SQLiteOpenHelper{
    public MyOpenHelper(Context context) {
        super(context, "lol.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists singers(_id integer primary key autoincrement,name varchar(20))");
        db.execSQL("insert into singers(name) values(?)",new String[]{"周杰伦"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"薛之谦"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"林俊杰"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"陈奕迅"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"李荣浩"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"华晨宇"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"毛不易"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"沈以城"});
        db.execSQL("insert into singers(name) values(?)",new String[]{"许嵩"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}