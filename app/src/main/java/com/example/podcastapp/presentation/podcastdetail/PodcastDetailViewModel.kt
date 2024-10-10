package com.example.podcastapp.presentation.podcastdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastapp.domain.model.Episode
import com.example.podcastapp.domain.model.Podcast
import com.example.podcastapp.domain.usecase.GetPodcastDetailsUseCase
import com.example.podcastapp.domain.usecase.ToggleFavoriteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class PodcastDetailViewModel(
    private val podcastUrl: String,
    private val getPodcastDetailsUseCase: GetPodcastDetailsUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    val podcastDetails: StateFlow<Podcast> = getPodcastDetailsUseCase(podcastUrl)
        .stateIn(viewModelScope, SharingStarted.Lazily, Podcast("", "", "", "", "", Date(), emptyList()))

    fun downloadEpisode(episode: Episode) {
        viewModelScope.launch {
        }
    }

    fun toggleFavorite(episode: Episode) {
        viewModelScope.launch {

        }
    }
}