package com.example.podcastapp.presentation.podcast_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastapp.domain.model.Podcast
import com.example.podcastapp.domain.usecase.AddPodcastUseCase
import com.example.podcastapp.domain.usecase.GetPodcastsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PodcastListViewModel(
    private val getPodcastsUseCase: GetPodcastsUseCase,
    private val addPodcastUseCase: AddPodcastUseCase
) : ViewModel() {

    private val _podcasts = MutableStateFlow<List<Podcast>>(emptyList())
    val podcasts: StateFlow<List<Podcast>> = _podcasts

    private val _addPodcastStatus = MutableStateFlow<AddPodcastStatus>(AddPodcastStatus.Idle)
    val addPodcastStatus: StateFlow<AddPodcastStatus> = _addPodcastStatus

    init {
        loadPodcasts()
    }

    private fun loadPodcasts() {
        viewModelScope.launch {
            getPodcastsUseCase()
                .catch { e ->
                    // Handle error
                }
                .collect { podcastList ->
                    _podcasts.value = podcastList
                }
        }
    }

    fun addPodcast(url: String) {
        viewModelScope.launch {
            _addPodcastStatus.value = AddPodcastStatus.Loading
            try {
                addPodcastUseCase(url)
                _addPodcastStatus.value = AddPodcastStatus.Success
                loadPodcasts() // Reload the list after adding a new podcast
            } catch (e: Exception) {
                _addPodcastStatus.value =
                    AddPodcastStatus.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    sealed class AddPodcastStatus {
        object Idle : AddPodcastStatus()
        object Loading : AddPodcastStatus()
        object Success : AddPodcastStatus()
        data class Error(val message: String) : AddPodcastStatus()
    }
}