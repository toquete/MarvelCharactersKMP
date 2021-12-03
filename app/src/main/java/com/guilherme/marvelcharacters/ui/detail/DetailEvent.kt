package com.guilherme.marvelcharacters.ui.detail

import androidx.annotation.StringRes
import com.guilherme.marvelcharacters.infrastructure.Event

sealed class DetailEvent : Event {
    data class ShowSnackbarMessage(@StringRes val message: Int, val showAction: Boolean) : DetailEvent()
}
