package com.example.musicplayer.Model

import java.io.Serializable

data class Music(
    val id: Int,
    val songName: String?,
    val artistName: String?,
    val time: String?,
    val url: String?
) : Serializable {
}


