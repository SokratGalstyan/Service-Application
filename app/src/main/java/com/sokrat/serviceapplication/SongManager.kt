package com.sokrat.serviceapplication

import kotlin.math.abs

data class Song(
    val name: String,
    val artist: String,
    val id: Int,
)

class SongManager{
    private var currentSongIndex = 0
    private val songList = listOf<Song>(
        Song("Caramelo", "Ozuna", R.raw.caramelo),
        Song("Sugar", "Maroon 5", R.raw.sugar),
        Song("Lovely", "Billie Eilish, Khalid", R.raw.lovely)
    )

    fun getNextSong() = songList[++currentSongIndex % 3]
    fun getPreviousSong() = songList[abs(--currentSongIndex % 3)]
}