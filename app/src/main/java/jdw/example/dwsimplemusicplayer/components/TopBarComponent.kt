package jdw.example.dwsimplemusicplayer.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarComponent(title: MutableState<String>) {
    TopAppBar(title = { Text(title.value, fontSize = 20.sp) },
        navigationIcon = {
        },
        actions = {
        }
    )
}
