package jdw.example.dwsimplemusicplayer.screens.main

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import jdw.example.dwsimplemusicplayer.MainActivity
import jdw.example.dwsimplemusicplayer.components.TopBarComponent
import jdw.example.dwsimplemusicplayer.navigation.Navigation
import jdw.example.dwsimplemusicplayer.navigation.screenInBottom
import jdw.example.dwsimplemusicplayer.utils.hasReadExternalStoragePermission

@Composable
fun MainViewScreen(viewModel: MainViewModel, onBackKeyPressed: () -> Unit) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentScreen = remember { viewModel.currentScreen.value }
    val title = remember { mutableStateOf(currentScreen.title) }

    val bottomBar: @Composable () -> Unit = {
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

    val requestPermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->
            val isAllowedPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permission[Manifest.permission.READ_MEDIA_AUDIO]!!
            } else {
                permission[Manifest.permission.READ_EXTERNAL_STORAGE]!!
            }

            if (isAllowedPermission) {
                viewModel.loadAudioData()
            } else {
                val rationalRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                if (rationalRequired) {
                    Toast.makeText(
                        context,
                        "READ_EXTERNAL_STORAGE Permissions are required to run",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Need to enable READ_EXTERNAL_STORAGE permissions",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    val requestSinglePermissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
            if (isGranted) {
                viewModel.loadAudioData()
            } else {
                val rationalRequired = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.READ_MEDIA_AUDIO
                    )
                } else {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        context as MainActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
                if (rationalRequired) {
                    Toast.makeText(
                        context,
                        "READ_EXTERNAL_STORAGE Permissions are required to run",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        "Need to enable READ_EXTERNAL_STORAGE permissions",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }

    Scaffold(
        topBar = { TopBarComponent(title) },
        bottomBar = bottomBar,
    ) { paddingValue ->
        if (!hasReadExternalStoragePermission(context)) {
            Log.d("TAG", "hasReadExternalStoragePermission: false ")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestSinglePermissionLauncher.launch(
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            } else {
                requestPermissionLauncher.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
            }
        }
        Navigation(
            navController,
            viewModel = viewModel,
            pd = paddingValue,
            onBackKeyPressed = onBackKeyPressed
        )
    }


}
