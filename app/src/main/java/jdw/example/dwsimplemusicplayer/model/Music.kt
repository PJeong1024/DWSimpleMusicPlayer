package jdw.example.dwsimplemusicplayer.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_table")
data class Music(
    @PrimaryKey
    val id: Long = 0,
    val title: String = "",
    val displayName: String= "",
    val artist: String= "",
    val data: String= "",
    val duration: Long = 0,
    val downloadedDate: Long = 0,
    val uri: Uri = Uri.EMPTY,
    val albumId: Long = 0,
    )
