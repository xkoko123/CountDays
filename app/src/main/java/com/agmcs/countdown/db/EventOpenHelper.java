package com.agmcs.countdown.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by agmcs on 2015/3 /4.
 */
public class EventOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_EVENT = "create table event (" +
            "id integer primary key autoincrement," +
            "title text," +
            "date long," +
            "color int," +
            "note text)";

    public EventOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
