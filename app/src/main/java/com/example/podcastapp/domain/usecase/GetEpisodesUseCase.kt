package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.domain.model.Episode
import kotlinx.coroutines.flow.Flow

class GetEpisodesUseCase(private val podcastRepository: PodcastRepository) {
    operator fun invoke(podcastUrl: String): Flow<List<Episode>> {
        return podcastRepository.getEpisodes(podcastUrl)
    }
}