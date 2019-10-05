package com.guilherme.marvelcharacters

import android.app.Application
import com.guilherme.marvelcharacters.infrastructure.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CustomApplication)
            modules(appModule)
        }
    }
}