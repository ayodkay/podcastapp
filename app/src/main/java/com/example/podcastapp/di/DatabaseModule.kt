package com.example.podcastapp.di

import androidx.room.Room
import com.example.podcastapp.data.local.PodcastDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            PodcastDatabase::class.java,
            "podcast_database"
        ).build()
    }

    single { get<PodcastDatabase>().podcastDao() }
    single { get<PodcastDatabase>().episodeDao() }
}