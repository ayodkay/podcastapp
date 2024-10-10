package com.example.podcastapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "episodes")
data class EpisodeEntity(
    @PrimaryKey val guid: String,
    val podcastUrl: String,
    val title: String,
    val description: String,
    val audioUrl: String,
    val publishDate: Long,
    val duration: Long,
    val isDownloaded: Boolean = false,
    val isFavorite: Boolean = false
)