package com.example.podcastapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.podcastapp.data.local.dao.EpisodeDao
import com.example.podcastapp.data.local.dao.PodcastDao
import com.example.podcastapp.data.local.entity.EpisodeEntity
import com.example.podcastapp.data.local.entity.PodcastEntity

@Database(entities = [PodcastEntity::class, EpisodeEntity::class], version = 1)
abstract class PodcastDatabase : RoomDatabase() {
    abstract fun podcastDao(): PodcastDao
    abstract fun episodeDao(): EpisodeDao
}