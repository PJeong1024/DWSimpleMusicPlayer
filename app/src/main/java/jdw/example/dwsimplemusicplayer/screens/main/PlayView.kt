package jdw.example.dwsimplemusicplayer.screens.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.exoplayer.ExoPlayer
import jdw.example.dwsimplemusicplayer.R
import jdw.example.dwsimplemusicplayer.model.Music
import jdw.example.dwsimplemusicplayer.model.PlayList
import jdw.example.dwsimplemusicplayer.utils.getAlbumArtFromUri
import jdw.example.dwsimplemusicplayer.utils.getIdListFromMusicList
import jdw.example.dwsimplemusicplayer.utils.timeStampToDuration

@Composable
fun PlayView(onBackKeyPressed: () -> Unit, viewModel: MainViewModel) {
    val playMusicList = remember { viewModel.playMusicList }

    BackHandler(enabled = true, onBack = {
        onBackKeyPressed()
    })

    Column {
        PlayListTextBox(viewModel)
        LazyColumn {
            items(playMusicList.value) { music ->
                PlayMusicItemView(music, viewModel)
            }
        }
    }
}

@Composable
fun PlayListTextBox(viewModel: MainViewModel) {
    val exoPlayer: ExoPlayer
    val context = LocalContext.current
    var editedText by remember { mutableStateOf(viewModel.playMusicListInfo.value.listName) }

    Card(
        Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = Color.White)
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            Modifier
                .padding(3.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                Modifier
                    .padding(3.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_music_library),
                    contentDescription = ""
                )
                Text(text = "Playlist : ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                BasicTextField(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(200.dp)
                        .background(color = Color.LightGray),
                    value = editedText,
                    onValueChange = { inputText ->
                        editedText = inputText
                    },
                    singleLine = true,
                    maxLines = 1,
                    textStyle = TextStyle(fontSize = 20.sp)

                )
            }
            Row(
                Modifier
                    .padding(3.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.clickable {
                        val playList = viewModel.getMusicListInfo()
                        val storedPLName =
                            viewModel.playListList.value.find { pl -> pl.listName == editedText }
                        if (storedPLName?.listName == editedText) { // existed
                            Toast.makeText(context, "Updating the playlist", Toast.LENGTH_SHORT)
                                .show()
                            val updatedPL = PlayList(
                                id = playList.id,
                                listName = editedText,
                                musicList = getIdListFromMusicList(viewModel.playMusicList.value)
                            )
                            viewModel.updatePlayList(updatedPL)
                        } else {
                            Toast.makeText(context, "Adding the playlist", Toast.LENGTH_SHORT)
                                .show()
                            viewModel.addPlayList(
                                PlayList(
                                    listName = editedText,
                                    musicList = getIdListFromMusicList(viewModel.playMusicList.value)
                                )
                            )
                        }
                    },
                    painter = painterResource(id = R.drawable.baseline_save_as_24),
                    contentDescription = ""
                )
            }
        }
    }
}


@Composable
fun PlayMusicItemView(music: Music, viewModel: MainViewModel) {
    val context = LocalContext.current

    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .height(80.dp)
            .padding(3.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        val ibm =
            getAlbumArtFromUri(context, music.albumId)?.asImageBitmap()
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (ibm != null) {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .padding(10.dp),
                    bitmap = ibm,
                    contentDescription = "Album Art Image"
                )
            } else {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .padding(10.dp),
                    painter = painterResource(id = R.drawable.ic_music_library),
                    contentDescription = "Album Art Image"
                )
            }
            Column(
                modifier = Modifier
                    .width(210.dp)
                    .padding(5.dp)
            ) {
                Text(text = music.title, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(text = music.artist, maxLines = 1)
                Text(text = timeStampToDuration(music.duration), maxLines = 1)
            }
            Icon(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(5.dp)
                    .clickable { },
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play music"
            )
            Icon(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(5.dp)
                    .clickable { viewModel.removeMusicInList(context, music) },
                imageVector = Icons.Filled.Delete,
                contentDescription = "Remove music from playlist"
            )
        }
    }
}