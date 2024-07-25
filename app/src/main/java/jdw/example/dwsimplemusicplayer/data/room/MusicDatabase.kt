package jdw.example.dwsimplemusicplayer.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import jdw.example.dwsimplemusicplayer.model.Music
import jdw.example.dwsimplemusicplayer.model.PlayList
import jdw.example.dwsimplemusicplayer.utils.LongListConverter
import jdw.example.dwsimplemusicplayer.utils.UriConverter

@Database(entities = [Music::class, PlayList::class], version = 1, exportSchema = false)
@TypeConverters(LongListConverter::class, UriConverter::class)
abstract class MusicDatabase : RoomDatabase() {
    abstract fun musicDatabaseDao () : MusicDatabaseDao
}