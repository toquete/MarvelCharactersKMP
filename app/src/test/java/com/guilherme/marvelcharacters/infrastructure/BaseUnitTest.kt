package com.guilherme.marvelcharacters.infrastructure

import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class BaseUnitTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    open fun setUp() {
        MockKAnnotations.init(this)
    }
}