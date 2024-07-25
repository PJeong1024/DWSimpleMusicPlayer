package jdw.example.dwsimplemusicplayer.model

import androidx.compose.runtime.MutableState
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlayList(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,

    @ColumnInfo(name = "list_name")
    val listName : String = "",

    @ColumnInfo(name = "music_list")
    val musicList : List<Long> = emptyList()
)
