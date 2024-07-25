package jdw.example.dwsimplemusicplayer.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import jdw.example.dwsimplemusicplayer.R
import jdw.example.dwsimplemusicplayer.model.Music
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import kotlin.math.floor


@SuppressLint("SimpleDateFormat")
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy.MM.dd")
    val date = java.util.Date(timestamp.toLong() * 1000)
    return sdf.format(date)
}

fun getAlbumArt(contentResolver: ContentResolver, albumId: String): Bitmap? {
    val albumArtUri = Uri.parse("content://media/external/audio/albumart/$albumId")
    contentResolver.openInputStream(albumArtUri)?.use { inputStream ->
        return BitmapFactory.decodeStream(inputStream)
    }
    return null
}

//fun getMetadata(path : String) {
//    MediaMetadataRetriever
//}

@SuppressLint("Range")
fun getAlbumArtFromUri(context: Context, albumId: Long): Bitmap? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val contentUri = ContentUris.withAppendedId(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            albumId
        )
        try {
            context.contentResolver.loadThumbnail(contentUri, Size(100, 100), null)
        } catch (e: Exception) {
            BitmapFactory.decodeResource(context.resources, R.drawable.ic_music_library)
        }
    } else {
        // For devices before Android Q, you can use the deprecated method
        val projection = arrayOf(MediaStore.Audio.Albums.ALBUM_ART)
        val cursor = context.contentResolver.query(
            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Audio.Albums._ID + "=?",
            arrayOf(albumId.toString()),
            null
        )
        cursor?.use { c ->
            if (c.moveToFirst()) {
                val albumArt = c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                if (albumArt != null) {
                    BitmapFactory.decodeFile(albumArt)
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun FormatDateFromTimeStamp(timestamp: Long): String {
    val localDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
    val formatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") // Customize the date format here
    return localDateTime.format(formatter)
}

fun timeStampToDuration(position: Long): String {
    val totalSecond = floor(position / 1E3).toInt()
    val minutes = totalSecond / 60
    val remainingSeconds = totalSecond - (minutes * 60)
    return if (position < 0) "--:--"
    else "%d:%02d".format(minutes, remainingSeconds)
}

@SuppressLint("SimpleDateFormat")
fun formatDateTime(timestamp: Int): String {
    val sdf = SimpleDateFormat("hh:mm:aa")
    val date = java.util.Date(timestamp.toLong() * 1000)

    return sdf.format(date)
}

fun getTodayDateString() : String {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1) // Month is zero-based
    val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))

    return "$year$month$day" // Format as desired
}

fun formatDecimals(item: Double): String {
    return " %.0f".format(item)
}

fun hasReadExternalStoragePermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

fun getIdListFromMusicList(list : List<Music>) : List<Long> {
    return list.map{music -> music.id }
}