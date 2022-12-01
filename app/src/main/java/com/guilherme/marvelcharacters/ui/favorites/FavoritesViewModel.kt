package com.guilherme.marvelcharacters.ui.favorites

import android.database.sqlite.SQLiteException
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.BaseViewModel
import com.guilherme.marvelcharacters.mapper.CharacterMapper
import com.guilherme.marvelcharacters.model.CharacterVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase,
    private val mapper: CharacterMapper
) : BaseViewModel<FavoritesState, FavoritesEvent>(FavoritesState.initialState()) {

    init {
        viewModelScope.launch {
            getFavoriteCharactersUseCase()
                .map { list ->
                    list.map { mapper.mapTo(it) }
                }
                .collect { list ->
                    setState { it.copy(list = list) }
                }
        }
    }

    fun onDeleteAllClick() {
        viewModelScope.launch {
            try {
                deleteAllFavoriteCharactersUseCase()
                sendEvent(FavoritesEvent.ShowSnackbarMessage(R.string.character_deleted))
            } catch (exception: SQLiteException) {
                sendEvent(FavoritesEvent.ShowSnackbarMessage(R.string.error_message))
            }
        }
    }

    fun onFavoriteItemClick(character: CharacterVO) {
        viewModelScope.launch {
            sendEvent(FavoritesEvent.NavigateToDetail(character))
        }
    }
}
