package com.example.podcastapp.data.mapper

import com.example.podcastapp.data.local.entity.EpisodeEntity
import com.example.podcastapp.domain.model.Episode
import java.util.Date

fun Episode.toEntity() = EpisodeEntity(
    guid = guid,
    title = title,
    description = description,
    audioUrl = audioUrl,
    publishDate = publicationDate.time,
    duration = duration,
    podcastUrl = podcastUrl,
    isDownloaded = isDownloaded,
    isFavorite = isFavorite
)

fun EpisodeEntity.toDomainModel() = Episode(
    guid = guid,
    title = title,
    description = description,
    audioUrl = audioUrl,
    publicationDate = Date(publishDate),
    duration = duration,
    podcastUrl = podcastUrl,
    isDownloaded = isDownloaded,
    isFavorite = isFavorite
)