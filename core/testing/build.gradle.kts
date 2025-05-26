plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.guilherme.marvelcharacters.core.testing"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
    }
    compileOptions {
        sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
    }
    kotlinOptions {
        jvmTarget = rootProject.extra["jvmTargetVersion"].toString()
    }
}

dependencies {
    // Test
    api(libs.mockk)
    api(libs.junit)
    api(libs.kotlinx.coroutines.test)
    api(libs.truth)
    api(libs.turbine)

    // Android Test
    api(libs.test.core)
    api(libs.test.core.ktx)
    api(libs.test.runner)
    api(libs.junit.ext)
    api(libs.espresso.core)
    api(libs.espresso.contrib)
    api(libs.navigation.testing)
    api(libs.mockk.android)
}
