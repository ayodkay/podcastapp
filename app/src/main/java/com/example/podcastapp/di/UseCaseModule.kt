package com.example.podcastapp.di

import com.example.podcastapp.domain.usecase.*
import org.koin.dsl.module


val useCaseModule = module {
    single { GetPodcastsUseCase(get()) }
    single { AddPodcastUseCase(get()) }
    single { GetPodcastDetailsUseCase(get()) }
    single { GetEpisodesUseCase(get()) }
    single { ToggleFavoriteUseCase(get()) }
    single { GetFavoriteEpisodesUseCase(get()) }
    single { SearchPodcastsUseCase(get()) }
}