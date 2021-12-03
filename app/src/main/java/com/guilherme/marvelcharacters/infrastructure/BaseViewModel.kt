package com.guilherme.marvelcharacters.infrastructure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<STATE : State, EVENT : Event>(
    initialState: STATE
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<STATE> = _state

    private val _event = Channel<EVENT>(Channel.BUFFERED)
    val event = _event.receiveAsFlow()

    fun setState(state: (STATE) -> STATE) {
        _state.update(state)
    }

    fun sendEvent(event: EVENT) {
        viewModelScope.launch {
            _event.send(event)
        }
    }
}