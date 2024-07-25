package jdw.example.dwsimplemusicplayer.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jdw.example.dwsimplemusicplayer.screens.main.PlayListView
import jdw.example.dwsimplemusicplayer.screens.main.PlayView
import jdw.example.dwsimplemusicplayer.screens.main.MusicListView
import jdw.example.dwsimplemusicplayer.screens.main.MainViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    viewModel: MainViewModel,
    pd: PaddingValues,
    onBackKeyPressed  :() -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = Screen.BottomScreen.Play.route,
        modifier = Modifier.padding(pd),
    ) {
//        composable(Screen.DrawerScreen.Account.route) {
//            AccountView()
//        }
//        composable(Screen.DrawerScreen.Subscription.route) {
//            Subscription()
//        }

        composable(Screen.BottomScreen.Play.route) {
            PlayView(onBackKeyPressed = onBackKeyPressed, viewModel)
        }

        composable(Screen.BottomScreen.MusicList.route) {
            MusicListView(onBackKeyPressed = onBackKeyPressed, viewModel)
        }

        composable(Screen.BottomScreen.PlayList.route) {
            PlayListView(onBackKeyPressed = onBackKeyPressed, viewModel)
        }
    }
}