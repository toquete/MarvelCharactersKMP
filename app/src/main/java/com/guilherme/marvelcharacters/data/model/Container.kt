package com.guilherme.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class Container(@SerializedName("results") val characters: List<Character>)