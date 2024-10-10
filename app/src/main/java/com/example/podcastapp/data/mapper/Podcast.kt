package com.example.podcastapp.data.mapper

import com.example.podcastapp.data.local.entity.PodcastEntity
import com.example.podcastapp.domain.model.Episode
import com.example.podcastapp.domain.model.Podcast
import java.util.Date

fun Podcast.toEntity() = PodcastEntity(
    url = url,
    title = title,
    description = description,
    imageUrl = imageUrl,
    author = author,
    lastUpdated = lastUpdated.time
)

fun PodcastEntity.toDomainModel() = Podcast(
    url = url,
    title = title,
    description = description,
    imageUrl = imageUrl,
    author = author,
    lastUpdated = Date(lastUpdated),
    episodes = emptyList() // Note: episodes are loaded separately
)

fun Podcast.withEpisodes(episodes: List<Episode>) = copy(episodes = episodes)