package com.guilherme.marvelcharacters.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(@SerialName("data") val container: ContainerResponse<T>)

@Serializable
data class ContainerResponse<T>(@SerialName("results") val results: List<T>)