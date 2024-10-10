package com.example.podcastapp.domain.model

import java.util.Date

data class Episode(
    val guid: String,
    val title: String,
    val description: String,
    val audioUrl: String,
    val publicationDate: Date,
    val duration: Long,
    val podcastUrl: String,
    val isDownloaded: Boolean = false,
    val isFavorite: Boolean = false
)