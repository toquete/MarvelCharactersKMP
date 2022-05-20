package com.guilherme.marvelcharacters.ui.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    @Assisted private val character: Character,
    isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase
) : BaseViewModel<DetailState, DetailEvent>(DetailState()) {

    init {
        viewModelScope.launch {
            isCharacterFavoriteUseCase(character.id)
                .collect { isFavorite ->
                    setState { it.copy(isFavorite = isFavorite) }
                }
        }
    }

    fun onFabClick() {
        viewModelScope.launch {
            sendEvent(
                try {
                    if (state.value.isFavorite) {
                        deleteFavoriteCharacterUseCase(character.id)
                        DetailEvent.ShowSnackbarMessage(R.string.character_deleted, showAction = true)
                    } else {
                        insertFavoriteCharacterUseCase(character)
                        DetailEvent.ShowSnackbarMessage(R.string.character_added, showAction = false)
                    }
                } catch (error: SQLiteException) {
                    DetailEvent.ShowSnackbarMessage(R.string.error_message, showAction = false)
                }
            )
        }
    }

    fun onUndoClick() {
        viewModelScope.launch {
            try {
                insertFavoriteCharacterUseCase(character)
            } catch (error: SQLiteException) {
                sendEvent(DetailEvent.ShowSnackbarMessage(R.string.error_message, showAction = false))
            }
        }
    }
}