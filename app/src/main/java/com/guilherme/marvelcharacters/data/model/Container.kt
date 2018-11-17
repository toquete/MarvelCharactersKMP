package com.guilherme.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class Container(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val characters: List<Character>
)