package jdw.example.dwsimplemusicplayer.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jdw.example.dwsimplemusicplayer.R
import jdw.example.dwsimplemusicplayer.model.Music
import jdw.example.dwsimplemusicplayer.utils.getAlbumArtFromUri
import jdw.example.dwsimplemusicplayer.utils.timeStampToDuration

@Composable
fun MusicListView(onBackKeyPressed: () -> Unit, viewModel: MainViewModel) {
    val musicList = remember { viewModel.displayMusicListState }
    BackHandler(enabled = true, onBack = {
        onBackKeyPressed()
    })

    Column {
        MenuBox(viewModel)
        LazyColumn {
            items(musicList.value) { music ->
                MusicItemView(music, viewModel)
            }
        }
    }
}

@Composable
fun MenuBox(viewModel: MainViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val items = listOf("Latest", "Oldest", "Name(A~Z)", "Name(Z~A)")
    var selectedIndex by remember { mutableIntStateOf(0) }
    var editedText by remember { mutableStateOf("") }
    viewModel.sortMusicList(selectedIndex)

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
            Row {
                Icon(Icons.Filled.Search, contentDescription = "")
                BasicTextField(
                    value = editedText,
                    onValueChange = { inputText ->
                        editedText = inputText
                        viewModel.findMusicList(editedText)
                    },
                    singleLine = true,
                    modifier = Modifier
                        .width(100.dp)
                        .height(30.dp)
                        .padding(3.dp)
                        .background(color = Color.LightGray),
                )
            }
            Row {
                Text(text = "Sort by : ")
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(40.dp)
                        .background(
                            Color.LightGray
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row {
                        Icon(Icons.Filled.ArrowDropDown, contentDescription = "")
                        Text(
                            items[selectedIndex],
                            modifier = Modifier
                                .clickable(onClick = { expanded = true }),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        items.forEachIndexed { index, s ->
                            DropdownMenuItem(onClick = {
                                selectedIndex = index
                                expanded = false
                                viewModel.sortMusicList(selectedIndex)
                            }) {

                                Text(
                                    text = s,
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MusicItemView(music: Music, viewModel: MainViewModel) {
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
                    .width(250.dp)
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
                    .padding(10.dp)
                    .clickable { viewModel.addMusicInList(context, music) },
                imageVector = Icons.Filled.Add,
                contentDescription = "Add music to playlist",

            )
        }

//        Text(text = music.id.toString())
//        Text(text = music.albumId.toString())
//        Text(text = formatDate(music.downloadedDate))
//        Text(text = music.uri.toString())
//        Text(text = music.data)
//        Text(text = music.displayName)
    }
}
