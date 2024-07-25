package jdw.example.dwsimplemusicplayer.navigation

import androidx.annotation.DrawableRes
import jdw.example.dwsimplemusicplayer.R

sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreen(
        val bTitle: String,
        val bRoute: String,
        @DrawableRes val icon: Int
    ) : Screen(bTitle, bRoute) {
        object Play : BottomScreen(
            bTitle = "Play",
            bRoute = "play",
            R.drawable.ic_home
        )
        object MusicList : BottomScreen(
            bTitle = "Musiclist",
            bRoute = "musiclist",
            R.drawable.ic_music_library
        )
        object PlayList : BottomScreen(
            bTitle = "Playlist",
            bRoute = "playlist",
            R.drawable.ic_browse
        )
    }

//    sealed class DrawerScreen(
//        val dTitle: String,
//        val dRoute: String,
//        @DrawableRes val icon: Int
//    ) : Screen(dTitle, dRoute) {
//        object Account : DrawerScreen(
//            dTitle = "Account",
//            dRoute = "account",
//            R.drawable.ic_account
//
//        )
//
//        object Subscription : DrawerScreen(
//            dTitle = "Subscription",
//            dRoute = "subscribe",
//            R.drawable.ic_subscribe
//
//        )
//
//        object AddAccount : DrawerScreen(
//            dTitle = "Add Account",
//            dRoute = "add_account",
//            R.drawable.baseline_person_add_alt_1_24
//        )
//
//    }

}

//val screenInDrawer = listOf(
//    Screen.DrawerScreen.Account,
//    Screen.DrawerScreen.Subscription,
//    Screen.DrawerScreen.AddAccount,
//)

val screenInBottom = listOf(
    Screen.BottomScreen.Play,
    Screen.BottomScreen.MusicList,
    Screen.BottomScreen.PlayList,
)
