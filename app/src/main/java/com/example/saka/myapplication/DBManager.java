package com.example.saka.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.saka.myapplication.model.User;

/**
 * Created by Saka on 07-Jun-17.
 */

public class DBManager extends SQLiteOpenHelper {

    public DBManager(Context context) {
        super(context, "birdy.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table tbUser (_id int primary key autoincrement, _name text," +
                " _email text, uid text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertUser(User user){
        SQLiteDatabase database = getReadableDatabase();
        ContentValues row = new ContentValues();
        row.put("_uid", user.getUid());
        row.put("_name", user.getName());
        row.put("_email", user.getEmail());
    }
}
