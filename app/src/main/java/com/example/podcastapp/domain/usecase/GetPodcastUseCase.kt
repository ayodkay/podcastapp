package com.example.podcastapp.domain.usecase

import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.domain.model.Podcast
import com.example.podcastapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPodcastUseCase(private val podcastRepository: PodcastRepository) {

    operator fun invoke(url: String): Flow<Resource<Podcast>> = flow {
        emit(Resource.Loading())
        try {
            val podcast = podcastRepository.getPodcastWithEpisodes(url)
            if (podcast != null) {
                emit(Resource.Success(podcast))
            } else {
                emit(Resource.Error("Podcast not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}