package com.guilherme.marvelcharacters.infrastructure.di

import com.guilherme.marvelcharacters.ui.detail.DetailViewModelFactory
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@EntryPoint
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryProvider {

    fun detailViewModelFactory(): DetailViewModelFactory
}