package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.domain.model.Episode
import kotlinx.coroutines.flow.Flow

class GetFavoriteEpisodesUseCase(private val podcastRepository: PodcastRepository) {
    operator fun invoke(): Flow<List<Episode>> {
        return podcastRepository.getFavoriteEpisodes()
    }
}
