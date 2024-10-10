package com.example.podcastapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "podcasts")
data class PodcastEntity(
    @PrimaryKey val url: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val author: String,
    val lastUpdated: Long = System.currentTimeMillis()
)