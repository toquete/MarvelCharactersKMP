package com.guilherme.marvelcharacters.ui.mapper

interface Mapper<S, T> {

    fun mapTo(source: S): T

    fun mapFrom(origin: T): S
}