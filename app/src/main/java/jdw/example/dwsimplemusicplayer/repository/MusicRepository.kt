package jdw.example.dwsimplemusicplayer.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.media.session.MediaSession
import android.os.Build
import android.provider.MediaStore
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import jdw.example.dwsimplemusicplayer.data.room.MusicDatabaseDao
import jdw.example.dwsimplemusicplayer.model.Music
import jdw.example.dwsimplemusicplayer.model.PlayList
import jdw.example.dwsimplemusicplayer.utils.getTodayDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusicRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val musicDatabaseDao: MusicDatabaseDao,
) {
    // contact ContentResolver
    suspend fun fetchMusicFromDevice(): MutableList<Music> {
        val musicList = mutableListOf<Music>()
        withContext(Dispatchers.IO) {
            val contentResolver: ContentResolver = context.contentResolver

            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.ALBUM_ID,
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
            val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val cursor = contentResolver.query(
                collection,
                projection,
                selection,
                null,
                sortOrder
            )

            cursor?.use { cursor ->
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
                val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
                val displayNameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val downloadedDateColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_ADDED)
                val albumIdColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)

                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)
                    val duration = cursor.getLong(durationColumn)
                    val path = cursor.getString(dataColumn)
                    val displayName = cursor.getString(displayNameColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id
                    )
                    val downloadedDate = cursor.getLong(downloadedDateColumn)
                    val albumId = cursor.getLong(albumIdColumn)
                    val music =
                        Music(
                            id,
                            title,
                            displayName,
                            artist,
                            path,
                            duration,
                            downloadedDate,
                            uri,
                            albumId
                        )
                    if (musicDatabaseDao.getMusicById(music.id) == null) {
                        musicDatabaseDao.insertMusic(music)

                    }
                    musicList.add(music)
                }
            }
        }
        return musicList
    }

    // for Music
    suspend fun insertMusic(music: Music) {
        musicDatabaseDao.insertMusic(music)
    }

    suspend fun updateMusic(music: Music) {
        musicDatabaseDao.updateMusic(music)
    }

    suspend fun deleteMusicById(id: Long) {
        musicDatabaseDao.deleteMusicById(id)
    }

    suspend fun deleteAllMusic() {
        musicDatabaseDao.deleteAllFromDB()
    }

    fun getAllMusic(): Flow<List<Music>> {
        return musicDatabaseDao.getAllMusicFromDB()
    }

    fun getMusicById(id: Long): Music {
        return musicDatabaseDao.getMusicById(id)
    }

    //for PlayList
    fun checkIsSetPl(listName : String) : Boolean{
        val sharedPref =
            context.getSharedPreferences(
                "musicApp_preferences",
                Context.MODE_PRIVATE
            )
        return sharedPref.getString("musicListName", "").equals(listName)
    }

    suspend fun addPlayList(playList: PlayList) {
        musicDatabaseDao.insertPlayList(playList)
        val sharedPref =
            context.getSharedPreferences(
                "musicApp_preferences",
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putString("musicListName", playList.listName)
        editor.apply()
        editor.clear()
    }

    suspend fun deletePlayList(playList: PlayList) {
        val sharedPref =
            context.getSharedPreferences(
                "musicApp_preferences",
                Context.MODE_PRIVATE
            )
        if (checkIsSetPl(playList.listName)) {
            val editor = sharedPref.edit()
            editor.putString("musicListName", "")
            editor.apply()
            editor.clear()
        }
        musicDatabaseDao.deletePlayList(playList)
    }

    suspend fun updatePlayList(playList: PlayList) {
        musicDatabaseDao.updatePlayList(playList)
        val sharedPref =
            context.getSharedPreferences(
                "musicApp_preferences",
                Context.MODE_PRIVATE
            )
        val editor = sharedPref.edit()
        editor.putString("musicListName", playList.listName)
        editor.apply()
        editor.clear()
    }

    fun getAllPlayList(): Flow<List<PlayList>> =
        musicDatabaseDao.getAllPlayList().flowOn(Dispatchers.IO).conflate()

    fun getInitPlayList(): Flow<PlayList> {
        val sharedPref = context.getSharedPreferences("musicApp_preferences", Context.MODE_PRIVATE)
        val musicListName = sharedPref.getString("musicListName", "")
        return if (musicListName != "") {
            musicDatabaseDao.getPlayListByName(musicListName!!)
        } else {
            val defaultPlayList = PlayList(listName = "PlayList-" + getTodayDateString())
            flowOf(defaultPlayList)
        }
    }
}