package com.guilherme.marvelcharacters.data.model

import com.google.gson.annotations.SerializedName

data class Result(@SerializedName("data") val container: Container)