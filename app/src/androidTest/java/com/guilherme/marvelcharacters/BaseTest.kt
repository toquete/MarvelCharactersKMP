package com.guilherme.marvelcharacters

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.guilherme.marvelcharacters.data.source.local.CharacterDatabase
import com.guilherme.marvelcharacters.data.service.Api
import org.junit.After
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

@RunWith(AndroidJUnit4::class)
abstract class BaseTest : KoinTest {

    protected val api: Api by inject()
    protected val db: CharacterDatabase by inject()

    @After
    open fun tearDown() {
        db.close()
        stopKoin()
    }
}