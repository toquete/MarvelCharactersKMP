package com.guilherme.marvelcharacters

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import io.mockk.verify
import org.junit.Rule
import org.junit.Test

class EventTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `getContentIfNotHandled - retorna conteúdo`() {
        val event = Event("teste")

        assertThat(event.getContentIfNotHandled()).isEqualTo("teste")
    }

    @Test
    fun `getContentIfNotHandled - retorna nulo`() {
        val event = Event("teste")

        // simula o envio de um evento
        event.getContentIfNotHandled()

        assertThat(event.getContentIfNotHandled()).isNull()
    }

    @Test
    fun `peekContent - retorna conteúdo`() {
        val event = Event("teste")

        assertThat(event.peekContent()).isEqualTo("teste")
    }

    @Test
    fun `hasBeenHandled - retorna false para evento não manipulado`() {
        val event = Event("teste")

        assertThat(event.hasBeenHandled).isFalse()
    }

    @Test
    fun `hasBeenHandled - retorna true para evento manipulado`() {
        val event = Event("teste")

        event.getContentIfNotHandled()

        assertThat(event.hasBeenHandled).isTrue()
    }

    @Test
    fun `eventObserver - executa código quando evento não manipulado`() {
        val liveData = MutableLiveData<Event<Int>>()
        val mock = mockk<(Int) -> Unit>(relaxed = true)
        val eventObserver = EventObserver<Int> { mock(it) }
        val event = Event(0)

        liveData.observeForever(eventObserver)

        liveData.value = event

        verify { mock(0) }

        liveData.removeObserver(eventObserver)
    }

    @Test
    fun `eventObserver - não executa código quando evento já manipulado`() {
        val liveData = MutableLiveData<Event<Int>>()
        val mock = mockk<(Int) -> Unit>(relaxed = true)
        val eventObserver = EventObserver<Int> { mock(it) }
        val event = Event(0)

        liveData.observeForever(eventObserver)

        liveData.value = event
        liveData.value = event

        verify(exactly = 1) { mock(0) }

        liveData.removeObserver(eventObserver)
    }
}