package jdw.example.dwsimplemusicplayer.screens.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jdw.example.dwsimplemusicplayer.repository.MusicRepository
import jdw.example.dwsimplemusicplayer.model.Music
import jdw.example.dwsimplemusicplayer.model.PlayList
import jdw.example.dwsimplemusicplayer.navigation.Screen
import jdw.example.dwsimplemusicplayer.utils.getTodayDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MusicRepository,
) : ViewModel() {
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.BottomScreen.Play)

    // use in Play screen
    var playMusicList: MutableState<List<Music>> = mutableStateOf(listOf<Music>())
    var playMusicListInfo: MutableState<PlayList> =
        mutableStateOf(PlayList(listName = "PlayList-" + getTodayDateString()))

    // use in MusicList screen
    private var musicList: MutableState<List<Music>> = mutableStateOf(listOf<Music>())
    val displayMusicListState: MutableState<List<Music>> = mutableStateOf(listOf<Music>())

    // use in Playlist screen
    val playListList = mutableStateOf(listOf<PlayList>())

    init {
        Log.d("TAG", "MainViewModel : init ")
        loadAudioData()
        loadPlayListData()
        initPlayList()
    }

    val currentScreen: MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }

    // use in Play screen
    fun addMusicInList(context: Context, music: Music) {
        if (playMusicList.value.contains(music)) {
            Toast.makeText(context, "Already added in play list", Toast.LENGTH_SHORT).show()
        } else {
            playMusicList.value += music
            Toast.makeText(context, "Added in play list successfully", Toast.LENGTH_SHORT).show()
        }
    }

    fun removeMusicInList(context: Context, music: Music) {
        playMusicList.value -= music
        Toast.makeText(context, "Removed in play list successfully", Toast.LENGTH_SHORT).show()
    }

    private fun initPlayList() {
        viewModelScope.launch(Dispatchers.IO) {
            playMusicListInfo =
                mutableStateOf(PlayList(listName = "PlayList-" + getTodayDateString()))
            repository.getInitPlayList().distinctUntilChanged().collect() { pl ->
                playMusicListInfo.value = pl

                if (playMusicListInfo.value.musicList.isEmpty()) {
                    Log.d("TAG", "initPlayList: empty list()")
                } else {
                    // do making music list with
                    playMusicList = mutableStateOf(listOf<Music>())
                    playMusicListInfo.value.musicList.forEach { idValue ->
                        playMusicList.value += repository.getMusicById(idValue)
                    }
                    Log.d("TAG", "done to initPlayList: ${playMusicList.value.size}")
                }
            }
        }
    }


    // use in MusicList screen
    fun loadAudioData() {
        Log.d("TAG", "loadAudioData ")
        viewModelScope.launch {
            musicList.value = repository.fetchMusicFromDevice()
            displayMusicListState.value = musicList.value
            Log.d("TAG", "done to get loadAudioData : ${musicList.value.size} ")
        }

    }

    fun sortMusicList(selectedIndex: Int) {
        displayMusicListState.value = when (selectedIndex) {
            0 -> musicList.value.sortedByDescending { music -> music.downloadedDate } // latest
            1 -> musicList.value.sortedBy { music -> music.downloadedDate } // oldest
            2 -> musicList.value.sortedBy { music -> music.title } // A~Z
            3 -> musicList.value.sortedByDescending { music -> music.title } // Z~A
            else -> musicList.value
        }
    }

    fun findMusicList(searchKeyword: String) {
        displayMusicListState.value = musicList.value.filter { music ->
            music.title.contains(searchKeyword, ignoreCase = true) ||
                    music.artist.contains(searchKeyword, ignoreCase = true)
        }
        Log.d("TAG", "findMusicList: " + musicList.value.size)
    }

    fun loadPlayMusicListData(listName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playMusicListInfo.value =
                playListList.value.find { playList -> playList.listName == listName }!!
            playMusicList.value = emptyList()

            if (playMusicListInfo.value.musicList.isEmpty()) {
                Log.d("TAG", "loadPlayMusicListData: empty list()")
            } else {
                // do making music list with
                playMusicListInfo.value.musicList.forEach { idValue ->
                    playMusicList.value += repository.getMusicById(idValue)
                }
            }
        }
    }

    // use in Playlist screen
    private fun loadPlayListData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPlayList().distinctUntilChanged().collect() { listOfPlayList ->
                Log.d("TAG", "loadPlayListData: ${listOfPlayList.size}")
                if (listOfPlayList.isEmpty()) {
                    Log.d("TAG", "loadPlayListData: empty list()")
                } else {
                    playListList.value = listOfPlayList
                }
            }
        }
    }

    fun addPlayList(playList: PlayList) {
        viewModelScope.launch {
            repository.addPlayList(playList)
            playMusicListInfo.value = playList
        }
    }

    fun deletePlayList(playList: PlayList) {
        viewModelScope.launch {
            if(repository.checkIsSetPl(playMusicListInfo.value.listName)) {
                playMusicListInfo =
                    mutableStateOf(PlayList(listName = "PlayList-" + getTodayDateString()))
                playMusicList = mutableStateOf(listOf<Music>())
            }

            playListList.value = playListList.value.filter { it != playList }
            repository.deletePlayList(playList)
        }
    }

    fun updatePlayList(playList: PlayList) {
        viewModelScope.launch {
            repository.updatePlayList(playList)
            playMusicListInfo.value = playList
        }
    }

    fun getMusicListInfo(): PlayList {
        return playMusicListInfo.value
    }
}