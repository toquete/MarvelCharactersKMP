package com.guilherme.marvelcharacters.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterVO(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String
) : Parcelable