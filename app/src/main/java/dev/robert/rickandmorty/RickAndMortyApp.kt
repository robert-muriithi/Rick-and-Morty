package dev.robert.rickandmorty

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class RickAndMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTimber()
    }
}

fun setupTimber(){
    Timber.plant(Timber.DebugTree())
}

