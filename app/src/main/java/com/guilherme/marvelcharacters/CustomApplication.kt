package com.guilherme.marvelcharacters

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.guilherme.marvelcharacters.infrastructure.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        startKoin {
            androidContext(this@CustomApplication)
            modules(appModule)
        }
    }
}