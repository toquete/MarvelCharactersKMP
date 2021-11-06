package com.guilherme.marvelcharacters.infrastructure.util

interface Mapper<S, T> {

    fun mapTo(source: S): T

    fun mapFrom(origin: T): S
}