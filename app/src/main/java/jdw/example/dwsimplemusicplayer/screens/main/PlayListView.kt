package jdw.example.dwsimplemusicplayer.screens.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import jdw.example.dwsimplemusicplayer.R
import jdw.example.dwsimplemusicplayer.model.PlayList

@Composable
fun PlayListView(onBackKeyPressed: () -> Unit, viewModel: MainViewModel) {
    val plList = remember { viewModel.playListList }

    BackHandler(enabled = true, onBack = {
        onBackKeyPressed()
    })

    Column {
        LazyColumn {
            items(plList.value) { pl ->
                PlItemView(pl, viewModel)
            }
        }
    }
}


@Composable
fun PlItemView(pl: PlayList, viewModel: MainViewModel) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxSize()
            .height(80.dp)
            .padding(3.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .padding(10.dp),
                painter = painterResource(id = R.drawable.ic_music_library),
                contentDescription = "Album Art Image"
            )
            Column(
                modifier = Modifier
                    .width(210.dp)
                    .padding(5.dp)
            ) {
                Text(text = pl.listName, fontWeight = FontWeight.SemiBold, maxLines = 1)
                Text(text = pl.musicList.size.toString() + " Songs", maxLines = 1)
            }
            Icon(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(5.dp)
                    .clickable { viewModel.loadPlayMusicListData(pl.listName) },
                imageVector = Icons.Filled.PlayArrow,
                contentDescription = "Play pl"
            )
            Icon(
                modifier = Modifier
                    .width(60.dp)
                    .height(60.dp)
                    .padding(5.dp)
                    .clickable { viewModel.deletePlayList(pl) },
                imageVector = Icons.Filled.Delete,
                contentDescription = "Remove playlist"
            )
        }
    }
}