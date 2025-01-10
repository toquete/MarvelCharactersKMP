package com.guilherme.marvelcharacters

import android.app.Application
import com.guilherme.marvelcharacters.cache.infrastructure.di.cacheModule
import com.guilherme.marvelcharacters.data.infrastructure.di.dataModule
import com.guilherme.marvelcharacters.domain.infrastructure.di.domainModule
import com.guilherme.marvelcharacters.feature.detail.infrastructure.di.detailModule
import com.guilherme.marvelcharacters.feature.favorites.infrastructure.di.favoritesModule
import com.guilherme.marvelcharacters.feature.home.infrastructure.di.homeModule
import com.guilherme.marvelcharacters.remote.infrastructure.di.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CustomApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@CustomApplication)
            modules(cacheModule, remoteModule, dataModule, domainModule, detailModule, favoritesModule, homeModule)
        }
    }
}