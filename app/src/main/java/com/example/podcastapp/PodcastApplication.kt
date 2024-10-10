package com.example.podcastapp

import android.app.Application
import com.example.podcastapp.di.appModule
import com.example.podcastapp.di.databaseModule
import com.example.podcastapp.di.networkModule
import com.example.podcastapp.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class PodcastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PodcastApplication)
            modules(listOf(appModule, databaseModule, networkModule, useCaseModule))
        }
    }
}