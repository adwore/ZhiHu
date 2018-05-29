package com.computer.mazhihuapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by computer on 2015/9/22.
 */
public class WebCachDbHelper extends SQLiteOpenHelper {

    public WebCachDbHelper(Context context,  SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "webCache.db", null, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Cache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
