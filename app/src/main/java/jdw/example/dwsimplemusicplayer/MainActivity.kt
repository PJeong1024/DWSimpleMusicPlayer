package jdw.example.dwsimplemusicplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dagger.hilt.android.AndroidEntryPoint
import jdw.example.dwsimplemusicplayer.screens.main.MainViewModel
import jdw.example.dwsimplemusicplayer.screens.main.MainViewScreen
import jdw.example.dwsimplemusicplayer.ui.theme.DWSimpleMusicPlayerTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel by viewModels()
            val context = LocalContext.current
            DWSimpleMusicPlayerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Just memorise all code to get permissions
                    MainViewScreen(viewModel = viewModel, onBackKeyPressed = {
                        finish()
                    })
                }

            }
        }
    }
}


