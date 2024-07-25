package jdw.example.dwsimplemusicplayer.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import jdw.example.dwsimplemusicplayer.model.Music
import jdw.example.dwsimplemusicplayer.model.PlayList
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicDatabaseDao {
    // for Music
    @Insert
    suspend fun insertMusic(music: Music)

    @Update
    suspend fun updateMusic(music: Music)

    @Query("DELETE FROM music_table WHERE id = :id")
    suspend fun deleteMusicById(id: Long)

    @Query("DELETE FROM music_table")
    suspend fun deleteAllFromDB()

    @Query("SELECT * FROM music_table")
    fun getAllMusicFromDB(): Flow<List<Music>>

    @Query("SELECT * FROM music_table WHERE id = :id")
    fun getMusicById(id:Long): Music


    // for PlayList
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlayList(playList: PlayList)

    @Delete
    suspend fun deletePlayList(playList: PlayList)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePlayList(playList: PlayList)

    @Query("SELECT * FROM 'playlist_table'")
    fun getAllPlayList(): Flow<List<PlayList>>

    @Query("SELECT * FROM 'playlist_table' WHERE list_name =:listName")
    fun getPlayListByName(listName: String): Flow<PlayList>

//    @Query("SELECT COUNT(*) FROM 'playlist_table' WHERE list_name =:listName")
//    fun isExistedPlayList(listName: String): Int
}