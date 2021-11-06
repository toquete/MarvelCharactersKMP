package com.guilherme.marvelcharacters.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CharacterVO(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: ImageVO
) : Parcelable

@Parcelize
data class ImageVO(val path: String, val extension: String) : Parcelable