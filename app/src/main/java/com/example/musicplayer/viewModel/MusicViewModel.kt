package com.example.musicplayer.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.musicplayer.Model.Music
import com.example.musicplayer.Reposistory.MusicListReposistory


class MusicViewModel : ViewModel() {
    fun insert(context: Context, musicList: Music) {
        Log.d("TAG", "insert: '" + "Call")
        Log.d("Data", "insert: '" + musicList.id + musicList.songName)
        MusicListReposistory.insert(context, musicList)
    }

    fun getAllMusic(context: Context): LiveData<List<Music>> {
        var musicList = MusicListReposistory.getAllMusic(context)
        Log.d("TAG", "getAllMusic: "+musicList.value)
        return musicList
    }
}