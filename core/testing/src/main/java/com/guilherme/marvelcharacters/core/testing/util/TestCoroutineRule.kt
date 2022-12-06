package com.guilherme.marvelcharacters.core.testing.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@ExperimentalCoroutinesApi
class TestCoroutineRule : TestRule {

    val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val testCoroutinesScope = TestCoroutineScope(testCoroutineDispatcher)

    override fun apply(base: Statement, description: Description): Statement = object : Statement() {
        override fun evaluate() {
            Dispatchers.setMain(testCoroutineDispatcher)

            base.evaluate()

            Dispatchers.resetMain()
            testCoroutineDispatcher.cleanupTestCoroutines()
        }
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) = testCoroutinesScope.runBlockingTest { block() }
}