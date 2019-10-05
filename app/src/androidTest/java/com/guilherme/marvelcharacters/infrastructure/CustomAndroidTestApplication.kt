package com.guilherme.marvelcharacters.infrastructure

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CustomAndroidTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@CustomAndroidTestApplication)
            modules(testModule)
        }
    }
}