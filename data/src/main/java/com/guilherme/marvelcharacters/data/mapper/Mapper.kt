package com.guilherme.marvelcharacters.data.mapper

interface Mapper<S, T> {

    fun mapTo(source: S): T

    fun mapFrom(origin: T): S
}