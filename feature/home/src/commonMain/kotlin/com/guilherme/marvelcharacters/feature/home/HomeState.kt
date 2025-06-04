package com.guilherme.marvelcharacters.feature.home

import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.Resources
import org.jetbrains.compose.resources.StringResource

internal data class HomeState(
    val characters: List<Character> = emptyList(),
    val errorMessageId: StringResource? = Resources.String.StartMessage,
    val isLoading: Boolean = false
)
