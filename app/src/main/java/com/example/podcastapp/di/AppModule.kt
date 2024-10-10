package com.example.podcastapp.di


import com.example.podcastapp.data.repository.PodcastRepository
import com.example.podcastapp.data.repository.PodcastRepositoryImpl
import com.example.podcastapp.domain.usecase.GetPodcastUseCase
import com.example.podcastapp.presentation.MainViewModel
import com.example.podcastapp.presentation.player.PlayerViewModel
import com.example.podcastapp.presentation.podcast.PodcastViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get(), get()) }
    viewModel { PodcastViewModel(get(), get()) }
    viewModel { PlayerViewModel() }

    single<PodcastRepository> { PodcastRepositoryImpl(get(), get(), get()) }
    single { GetPodcastUseCase(get()) }
}