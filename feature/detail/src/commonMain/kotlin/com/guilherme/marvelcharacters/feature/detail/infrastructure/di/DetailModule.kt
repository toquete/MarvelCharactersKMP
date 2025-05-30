package com.guilherme.marvelcharacters.feature.detail.infrastructure.di

import com.guilherme.marvelcharacters.feature.detail.DetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val detailModule = module {
    viewModelOf(::DetailViewModel)
}