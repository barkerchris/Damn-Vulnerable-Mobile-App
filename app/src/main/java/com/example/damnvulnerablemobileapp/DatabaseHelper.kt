package com.example.damnvulnerablemobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT);"
        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(username: String, password: String) {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_USERNAME, username)
        cv.put(COLUMN_PASSWORD, password)
        db.insert(TABLE_NAME, null, cv)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "userProfileDB.db"
        const val TABLE_NAME = "user_profile"
        const val COLUMN_ID = "_id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
    }
}