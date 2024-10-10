package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository

class ToggleFavoriteUseCase(private val podcastRepository: PodcastRepository) {
    suspend operator fun invoke(episodeGuid: String, isFavorite: Boolean) {
        podcastRepository.toggleFavorite(episodeGuid, isFavorite)
    }
}
