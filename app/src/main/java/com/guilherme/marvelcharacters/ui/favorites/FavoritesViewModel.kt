package com.guilherme.marvelcharacters.ui.favorites

import android.database.sqlite.SQLiteException
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.usecase.DeleteAllFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.domain.usecase.DeleteFavoriteCharacterUseCase
import com.guilherme.marvelcharacters.domain.usecase.GetFavoriteCharactersUseCase
import com.guilherme.marvelcharacters.infrastructure.di.annotation.IoDispatcher
import com.guilherme.marvelcharacters.ui.mapper.CharacterMapper
import com.guilherme.marvelcharacters.ui.model.CharacterVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    getFavoriteCharactersUseCase: GetFavoriteCharactersUseCase,
    private val deleteFavoriteCharacterUseCase: DeleteFavoriteCharacterUseCase,
    private val deleteAllFavoriteCharactersUseCase: DeleteAllFavoriteCharactersUseCase,
    private val mapper: CharacterMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    val list: StateFlow<List<CharacterVO>> = getFavoriteCharactersUseCase()
        .flowOn(dispatcher)
        .map { list ->
            list.map { mapper.mapTo(it) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun deleteCharacter(character: CharacterVO) = viewModelScope.launch {
        deleteFavoriteCharacterUseCase(mapper.mapFrom(character))
    }

    fun onDeleteAllClick() = viewModelScope.launch {
        try {
            deleteAllFavoriteCharactersUseCase()
            _events.send(Event.ShowSnackbarMessage(R.string.character_deleted))
        } catch (exception: SQLiteException) {
            _events.send(Event.ShowSnackbarMessage(R.string.error_message))
        }
    }

    fun onFavoriteItemClick(character: CharacterVO) {
        viewModelScope.launch {
            _events.send(Event.NavigateToDetail(character))
        }
    }

    sealed class Event {
        data class ShowSnackbarMessage(@StringRes val messageId: Int) : Event()
        data class NavigateToDetail(val character: CharacterVO) : Event()
    }
}
