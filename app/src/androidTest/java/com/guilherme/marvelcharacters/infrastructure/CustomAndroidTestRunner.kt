package com.guilherme.marvelcharacters.infrastructure

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class CustomAndroidTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, CustomAndroidTestApplication::class.java.name, context)
    }
}