package com.example.podcastapp.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.podcastapp.domain.usecase.GetPodcastUseCase
import com.example.podcastapp.domain.model.Podcast
import com.example.podcastapp.util.NetworkUtils
import com.example.podcastapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getPodcastUseCase: GetPodcastUseCase,
    private val networkUtils: NetworkUtils
) : ViewModel() {

    private val _networkState = MutableStateFlow(true)
    val networkState: StateFlow<Boolean> = _networkState

    private val _showOfflineDialog = MutableStateFlow(false)
    val showOfflineDialog: StateFlow<Boolean> = _showOfflineDialog

    private val _podcasts = MutableStateFlow<List<Podcast?>>(emptyList())
    val podcasts: StateFlow<List<Podcast?>> = _podcasts

    private val _currentPodcast = MutableStateFlow<Resource<Podcast>>(Resource.Loading())
    val currentPodcast: StateFlow<Resource<Podcast>> = _currentPodcast

    init {
        observeNetworkState()
    }

    private fun observeNetworkState() {
        viewModelScope.launch {
            networkUtils.observeNetworkState().collect { isAvailable ->
                _networkState.value = isAvailable
                if (!isAvailable) {
                    _showOfflineDialog.value = true
                }
            }
        }
    }

    fun onOfflineDialogDismissed() {
        _showOfflineDialog.value = false
    }

    fun loadPodcast(url: String) {
        viewModelScope.launch {
            getPodcastUseCase(url).collect { result ->
                _currentPodcast.value = result
                if (result is Resource.Success) {
                    result.data.let { podcast ->
                        _podcasts.value += podcast
                    }
                }
            }
        }
    }

    fun clearCurrentPodcast() {
        _currentPodcast.value = Resource.Success(null)
    }

    fun isOffline(): Boolean = !_networkState.value

    fun navigateToDownloads() {
        // This function would typically be used to trigger navigation in the UI
        // For now, we'll just print a message
        println("Navigating to downloads")
    }

    fun List<Podcast>?.orEmpty(): List<Podcast> {
        if (this == null) {
            return emptyList()
        }
        return this
    }
}