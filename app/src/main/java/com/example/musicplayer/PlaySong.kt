package com.example.musicplayer

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicplayer.Model.Music

class PlaySong : AppCompatActivity() {
    var musicList : Music?=null
    var position : Int?=null
    companion object {
        fun launchActivity(activity: Context, music: ArrayList<Music>,position : Int) {
            if (activity != null) {
                val intent = Intent(activity, PlaySong::class.java)
                intent.putExtra("music",music)
                intent.putExtra("position",position)
                activity.startActivity(intent)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)
        musicList = intent.getSerializableExtra("music") as Music
        position = intent.getIntExtra("position",-1)

    }
}