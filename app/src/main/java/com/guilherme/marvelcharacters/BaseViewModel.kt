package com.guilherme.marvelcharacters

import android.arch.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(coroutineContext: CoroutineContext) : ViewModel() {

    private val viewModelJob = Job()

    protected open val uiScope = CoroutineScope(coroutineContext + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}