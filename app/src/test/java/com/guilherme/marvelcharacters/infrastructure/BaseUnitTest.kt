package com.guilherme.marvelcharacters.infrastructure

import com.guilherme.marvelcharacters.core.testing.util.TestCoroutineRule
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