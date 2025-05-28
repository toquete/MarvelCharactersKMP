package com.guilherme.marvelcharacters

import com.guilherme.marvelcharacters.cache.infrastructure.di.cacheModule
import com.guilherme.marvelcharacters.data.infrastructure.di.dataModule
import com.guilherme.marvelcharacters.domain.infrastructure.di.domainModule
import com.guilherme.marvelcharacters.feature.detail.infrastructure.di.detailModule
import com.guilherme.marvelcharacters.feature.favorites.infrastructure.di.favoritesModule
import com.guilherme.marvelcharacters.feature.home.infrastructure.di.homeModule
import com.guilherme.marvelcharacters.remote.infrastructure.di.remoteModule

val appModules = listOf(
    dataModule,
    domainModule,
    detailModule,
    favoritesModule,
    homeModule
) + remoteModule + cacheModule