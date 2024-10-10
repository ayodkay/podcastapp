package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.domain.model.Podcast

class AddPodcastUseCase(private val podcastRepository: PodcastRepository) {
    suspend operator fun invoke(url: String): Result<Podcast> {
        return try {
            val podcast = podcastRepository.fetchAndSavePodcast(url)
            Result.success(podcast)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}