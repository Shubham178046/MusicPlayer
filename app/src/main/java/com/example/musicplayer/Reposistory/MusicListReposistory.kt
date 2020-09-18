package com.example.musicplayer.Reposistory

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.musicplayer.Database.DataBaseHandler
import com.example.musicplayer.Model.Music

class MusicListReposistory {

    companion object
    {
        val TAG = "MusicListReposistory"
        var loadMusic : ArrayList<Music>?=null
        fun insert(context: Context, musicList: Music) {
            Log.d(TAG, "insert: "+musicList.id + " " + musicList.songName + " " + musicList.artistName + " " + musicList.time + " " + musicList.url)
            val databaseHandler: DataBaseHandler = DataBaseHandler(context)
            databaseHandler.addMusic(musicList)
            loadMusic = ArrayList()
            loadMusic!!.add(musicList)
        }

        fun getAllMusic(context: Context): LiveData<List<Music>> {
            val musicList = MutableLiveData<List<Music>>()
            val databaseHandler: DataBaseHandler = DataBaseHandler(context)
            musicList.value = databaseHandler.loadMusic()
//            Log.d(TAG, "getAllMusic: "+loadMusic!!.size)
            //musicList.value = loadMusic
            Log.d(TAG, "getAllMusic: "+musicList.value)
            return musicList
        }
    }

}