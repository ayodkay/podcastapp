package com.example.podcastapp.domain.model

import java.util.Date

data class Podcast(
    val url: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val author: String,
    val lastUpdated: Date,
    val episodes: List<Episode> = emptyList()
)