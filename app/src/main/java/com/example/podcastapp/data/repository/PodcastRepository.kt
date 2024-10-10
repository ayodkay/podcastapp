package com.example.podcastapp.data.repository

import com.example.podcastapp.domain.model.Episode
import com.example.podcastapp.domain.model.Podcast
import kotlinx.coroutines.flow.Flow

interface PodcastRepository {
    fun getAllPodcasts(): Flow<List<Podcast>>
    suspend fun getPodcastWithEpisodes(url: String): Podcast?
    suspend fun insertPodcast(podcast: Podcast)
    suspend fun updateEpisode(episode: Episode)
    suspend fun toggleFavorite(episodeGuid: String, isFavorite: Boolean)
    fun getFavoriteEpisodes(): Flow<List<Episode>>
    fun searchPodcasts(query: String): Flow<List<Podcast>>
    fun getPodcastDetails(podcastUrl: String): Flow<Podcast>
    suspend fun fetchAndSavePodcast(url: String): Podcast
    fun getEpisodes(podcastUrl: String): Flow<List<Episode>>
}