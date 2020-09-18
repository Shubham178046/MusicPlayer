package com.example.musicplayer.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.Model.Music
import com.example.musicplayer.R

class MusicAdapter(
    private val context: Context,
    private var musicList: List<Music>,
    private val listener: Listener
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder =
        MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_music_list, parent, false)
        )

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musicList[position]
        Log.d(
            "Data",
            "onBindViewHolder: " + musicList[position].songName + " " + musicList[position].artistName + musicList[position].time
        )
        holder.musicId.text = music.id.toString()
        holder.songName.text = music.songName
        holder.artistName.text = music.artistName
        holder.time.text = music.time
    }

    override fun getItemCount(): Int = musicList.size

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicId : TextView = itemView.findViewById(R.id.txtCount)
        val songName: TextView = itemView.findViewById(R.id.txtSongName)
        val artistName: TextView = itemView.findViewById(R.id.txtSongArtist)
        val time: TextView = itemView.findViewById(R.id.txtSongTime)

        init {
            itemView.setOnClickListener {
                listener.onCardClickListener(adapterPosition)
            }
        }
    }
    interface Listener {
        fun onCardClickListener(position: Int)
    }
}