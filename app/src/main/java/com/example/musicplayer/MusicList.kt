package com.example.musicplayer

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicplayer.Adapter.MusicAdapter
import com.example.musicplayer.Database.DataBaseHandler
import com.example.musicplayer.Model.Music
import com.example.musicplayer.viewModel.MusicViewModel
import kotlinx.android.synthetic.main.activity_music_list.*
import java.util.concurrent.TimeUnit

class MusicList : AppCompatActivity() {
    private lateinit var musicList: ArrayList<Music>
    val MY_PERMISSION_RESULT = 1001
    var musicViewModel: MusicViewModel? = null
    private var musicAdapter: MusicAdapter? = null

    companion object {
        fun launchActivity(activity: Context) {
            if (activity != null) {
                val intent = Intent(activity, MusicList::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        checkPermission()
    }

    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSION_RESULT
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSION_RESULT
                )
            }
        } else {
            readExternalData()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            MY_PERMISSION_RESULT -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                        readExternalData()
                    } else {
                        Toast.makeText(this, "No Permission Granted", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    return
                }
            }
        }
    }

    fun initViews() {
        musicViewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
        musicViewModel!!.getAllMusic(this).observe(this, object : Observer<List<Music>> {
            override fun onChanged(t: List<Music>?) {
                for (i in 0 until t!!.size) {
                    Log.d(
                        "Music",
                        "onChanged: " + t!!.get(i).artistName + " " + t!!.get(i).songName + t!!.get(
                            i
                        ).time
                    )
                }

                /* musicAdapter?.apply {
                     setMusicData(t!!)
                 }*/
                musicAdapter = MusicAdapter(this@MusicList, t!!,object :  MusicAdapter.Listener{
                    override fun onCardClickListener(position: Int) {
                        PlaySong.launchActivity(this@MusicList , t!! as ArrayList<Music>, position)
                    }

                })
                rvMusicList.layoutManager = LinearLayoutManager(this@MusicList)
                rvMusicList.adapter = musicAdapter
                musicAdapter!!.notifyDataSetChanged()
            }

        })
        musicList = ArrayList()
    }
/*
    override fun onDestroy() {
        super.onDestroy()
        val databaseHandler: DataBaseHandler = DataBaseHandler(this)
        databaseHandler.deleteRecord()
    }*/

    private fun readExternalData() {
        Log.d("MusicList", "readExternalData: " + "Call")
        val contentResolver = contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor != null) {
            var i: Int = 0
            cursor.moveToFirst()
            while (cursor.moveToNext()) {
                val music: Music
                val songName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                val artistName =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val songUrl = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                Log.d("Time", "readExternalData: " + time)
                val secToMin = TimeUnit.MILLISECONDS.toMinutes(time)
                initViews()
                music = Music(i, songName, artistName, "$secToMin min", songUrl)
                musicViewModel!!.insert(this, music)
                musicList.add(music)
                i++
                Log.d("main", "readExternalData: ${music.artistName} ")
            }
            // musicViewModel.insert(this,musicList)
        }
    }
}