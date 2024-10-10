package com.example.podcastapp.presentation.podcast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastapp.domain.model.Episode
import com.example.podcastapp.domain.model.Podcast
import com.example.podcastapp.domain.usecase.GetPodcastUseCase
import com.example.podcastapp.util.DownloadStatus
import com.example.podcastapp.util.PodcastDownloadManager
import com.example.podcastapp.util.Resource
import kotlinx.coroutines.launch

class PodcastViewModel (
    private val getPodcastUseCase: GetPodcastUseCase,
    private val downloadManager: PodcastDownloadManager
) : ViewModel() {
    private val _podcastState = MutableLiveData<Resource<Podcast>>()
    val podcastState: LiveData<Resource<Podcast>> = _podcastState

    private val _downloadState = MutableLiveData<Map<String, DownloadStatus>>()
    val downloadState: LiveData<Map<String, DownloadStatus>> = _downloadState

    init {
        viewModelScope.launch {
            downloadManager.observeDownloads().collect { status ->
                val currentMap = _downloadState.value ?: emptyMap()
                val newMap = currentMap.toMutableMap()
                when (status) {
                    is DownloadStatus.Progress -> newMap[status.episodeGuid] = status
                    is DownloadStatus.Completed -> newMap[status.episodeGuid] = status
                    is DownloadStatus.Failed -> newMap[status.episodeGuid] = status
                }
                _downloadState.value = newMap
            }
        }
    }

    fun downloadEpisode(episode: Episode) {
        viewModelScope.launch {
            downloadManager.downloadEpisode(episode)
        }
    }
}