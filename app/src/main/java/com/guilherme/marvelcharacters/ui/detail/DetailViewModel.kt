package com.guilherme.marvelcharacters.ui.detail

import android.database.sqlite.SQLiteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.Event
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.InsertFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.IsCharacterFavoriteUseCase
import com.guilherme.marvelcharacters.infrastructure.di.annotation.IoDispatcher
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    @Assisted private val character: Character,
    isCharacterFavoriteUseCase: IsCharacterFavoriteUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val insertFavoriteCharacterUseCase: InsertFavoriteCharacterUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _snackbarMessage = MutableLiveData<Event<Pair<Int, Boolean>>>()
    val snackbarMessage: LiveData<Event<Pair<Int, Boolean>>> = _snackbarMessage

    val isCharacterFavorite: StateFlow<Boolean> = isCharacterFavoriteUseCase(character.id)
        .flowOn(dispatcher)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

    fun onFabClick() = viewModelScope.launch {
        _snackbarMessage.value = try {
            if (isCharacterFavorite.value) {
                deleteFavoriteCharacterUseCase(character)
                Event(R.string.character_deleted to true)
            } else {
                insertFavoriteCharacterUseCase(character)
                Event(R.string.character_added to false)
            }
        } catch (error: SQLiteException) {
            Event(R.string.error_message to false)
        }
    }

    fun onUndoClick() = viewModelScope.launch {
        try {
            insertFavoriteCharacterUseCase(character)
        } catch (error: SQLiteException) {
            _snackbarMessage.value = Event(R.string.error_message to false)
        }
    }
}