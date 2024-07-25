package jdw.example.dwsimplemusicplayer.di

import android.content.Context
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.session.MediaSession
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jdw.example.dwsimplemusicplayer.data.room.MusicDatabase
import jdw.example.dwsimplemusicplayer.data.room.MusicDatabaseDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Singleton
    @Provides
    fun provideMusicDao(musicDatabase: MusicDatabase): MusicDatabaseDao = musicDatabase.musicDatabaseDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MusicDatabase =
        Room.databaseBuilder(context, MusicDatabase::class.java, "music_db")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideAudioAttributes(): AudioAttributes = AudioAttributes.Builder()
        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
        .setUsage(C.USAGE_MEDIA)
        .build()

    @Provides
    @Singleton
    @UnstableApi
    fun provideExoPlayer(
        @ApplicationContext context: Context,
        audioAttributes: AudioAttributes,
    ): ExoPlayer = ExoPlayer.Builder(context)
        .setAudioAttributes(audioAttributes, true)
        .setHandleAudioBecomingNoisy(true)
        .setTrackSelector(DefaultTrackSelector(context))
        .build()


    @Provides
    @Singleton
    fun provideMediaSession(
        @ApplicationContext context: Context,
        player: ExoPlayer,
    ): MediaSession = MediaSession.Builder(context, player).build()

//    @Provides
//    @Singleton
//    fun provideNotificationManager(
//        @ApplicationContext context: Context,
//        player: ExoPlayer,
//    ): JetAudioNotificationManager = JetAudioNotificationManager(
//        context = context,
//        exoPlayer = player
//    )

//    @Provides
//    @Singleton
//    fun provideServiceHandler(exoPlayer: ExoPlayer): JetAudioServiceHandler =
//        JetAudioServiceHandler(exoPlayer)
}