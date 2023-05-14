package com.blockdev.birdy.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.blockdev.birdy.model.User

/**
 * Created by Saka on 07-Jun-17.
 */
class DBManager(context: Context?) : SQLiteOpenHelper(context, "birdy.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        val sql = "create table tbUser (_id int primary key autoincrement, _name text," +
            " _email text, uid text)"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun insertUser(user: User) {
        val database = readableDatabase
        val row = ContentValues()
        row.put("_uid", user.uid)
        row.put("_name", user.name)
        row.put("_email", user.email)
    }
}
