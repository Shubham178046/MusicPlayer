package com.example.musicplayer.Database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.musicplayer.Model.Music
import java.lang.Exception
import kotlin.collections.ArrayList

class DataBaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "MusicDatabase"
        private val TABLE_NAME = "Music"
        private val KEY_ID = "id"
        private val KEY_NAME = "songName"
        private val KEY_ARTIST_NAME = "artistName"
        private val KEY_TIME = "time"
        private val KEY_URI = "url"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE =
            ("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT NOT NULL UNIQUE," + KEY_ARTIST_NAME + " TEXT," + KEY_TIME + " TEXT," + KEY_URI + " TEXT" + ")")
        db!!.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME)
        onCreate(db)
    }

    fun addMusic(music: Music): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, music.songName)
        contentValues.put(KEY_ARTIST_NAME, music.artistName)
        contentValues.put(KEY_TIME, music.time)
        contentValues.put(KEY_URI, music.url)
        val success = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return success
    }

    fun loadMusic(): ArrayList<Music> {
        val musicList: ArrayList<Music> = ArrayList<Music>()
        val selectQuery: String = "SELECT * FROM $TABLE_NAME ORDER BY $KEY_ID ASC"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var songId: Int
        var songname: String
        var songArtist: String
        var songTime: String
        var songUri: String
        if (cursor.moveToFirst()) {
            do {
                songId = cursor.getInt(cursor.getColumnIndex("id"))
                songname = cursor.getString(cursor.getColumnIndex("songName"))
                songArtist = cursor.getString(cursor.getColumnIndex("artistName"))
                songTime = cursor.getString(cursor.getColumnIndex("time"))
                songUri = cursor.getString(cursor.getColumnIndex("url"))
                musicList.add(Music(songId, songname, songArtist, songTime, songUri))
            } while (cursor.moveToNext())
        }
        return musicList
    }

    fun deleteRecord() {
        val db = this.writableDatabase
        //db.delete(TABLE_NAME, null, null);
       // db.execSQL("TRUNCATE table " + TABLE_NAME);
       // db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME");

        db.execSQL("DELETE FROM " + DATABASE_NAME);
        db.close();
    }
}