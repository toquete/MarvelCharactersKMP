package com.guilherme.marvelcharacters.feature.home.infrastructure.di

import com.guilherme.marvelcharacters.feature.home.HomeViewModel
import com.guilherme.marvelcharacters.feature.home.NightModeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val homeModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::NightModeViewModel)

}