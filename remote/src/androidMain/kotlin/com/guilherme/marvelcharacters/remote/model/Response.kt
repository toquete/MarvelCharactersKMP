package com.guilherme.marvelcharacters.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Response<T>(@SerialName("data") val container: ContainerResponse<T>)

@Serializable
internal data class ContainerResponse<T>(@SerialName("results") val results: List<T>)