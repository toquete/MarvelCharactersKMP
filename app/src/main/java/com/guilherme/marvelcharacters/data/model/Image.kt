package com.guilherme.marvelcharacters.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(val path: String, val extension: String) : Parcelable