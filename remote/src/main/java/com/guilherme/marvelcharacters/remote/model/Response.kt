package com.guilherme.marvelcharacters.remote.model

import com.google.gson.annotations.SerializedName

data class Response<T>(@SerializedName("data") val container: ContainerResponse<T>)

data class ContainerResponse<T>(@SerializedName("results") val results: List<T>)