package com.guilherme.marvelcharacters.remote.mapper

interface Mapper<S, T> {

    fun mapTo(source: S): T

    fun mapFrom(origin: T): S
}