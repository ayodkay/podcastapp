package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.domain.model.Podcast
import kotlinx.coroutines.flow.Flow

class SearchPodcastsUseCase(private val podcastRepository: PodcastRepository) {
    operator fun invoke(query: String): Flow<List<Podcast>> {
        return podcastRepository.searchPodcasts(query)
    }
}
