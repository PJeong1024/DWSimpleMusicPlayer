package jdw.example.dwsimplemusicplayer.components

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import jdw.example.dwsimplemusicplayer.navigation.screenInBottom

@Composable
fun BottomBarComponent(navController: NavController, currentRoute : String, title: MutableState<String>) {
    BottomNavigation(Modifier.wrapContentSize(), backgroundColor = Color.LightGray) {
        screenInBottom.forEach { item ->
            val isSelected = currentRoute == item.route
            val tint = if (isSelected) Color.White else Color.Black
            BottomNavigationItem(
                selected = currentRoute == item.bRoute,
                onClick = {
                    navController.navigate(item.bRoute)
                    title.value = item.bTitle
                },
                icon = {
                    Icon(
                        tint = tint,
                        painter = painterResource(id = item.icon),
                        contentDescription = item.bTitle,
                    )
                },
                label = { Text(text = item.bTitle, color = tint) },
                selectedContentColor = Color.White,
                unselectedContentColor = Color.Black
            )
        }
    }
}