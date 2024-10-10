package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.domain.model.Podcast
import kotlinx.coroutines.flow.Flow

class GetPodcastsUseCase(private val podcastRepository: PodcastRepository) {
    operator fun invoke(): Flow<List<Podcast>> {
        return podcastRepository.getAllPodcasts()
    }
}