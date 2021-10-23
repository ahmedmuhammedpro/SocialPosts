package com.khair.socialposts

import android.app.Application
import timber.log.Timber

class PostsListApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}