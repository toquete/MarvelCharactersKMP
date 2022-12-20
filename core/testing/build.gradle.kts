plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace = "com.guilherme.marvelcharacters.core.testing"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
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
    api("io.mockk:mockk:${rootProject.extra["mockkVersion"]}")
    api("junit:junit:${rootProject.extra["junitVersion"]}")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:${rootProject.extra["coroutinesVersion"]}")
    api("com.google.truth:truth:${rootProject.extra["truthVersion"]}")
    api("app.cash.turbine:turbine:${rootProject.extra["turbineVersion"]}")

    // Android Test
    api("androidx.test:core:${rootProject.extra["testCoreVersion"]}")
    api("androidx.test:core-ktx:${rootProject.extra["testCoreVersion"]}")
    api("androidx.test:runner:${rootProject.extra["testRunnerVersion"]}")
    api("androidx.test.ext:junit:${rootProject.extra["junitExtVersion"]}")
    api("androidx.test.espresso:espresso-core:${rootProject.extra["espressoVersion"]}")
    api("androidx.test.espresso:espresso-contrib:${rootProject.extra["espressoVersion"]}")
    api("androidx.navigation:navigation-testing:${rootProject.extra["navigationVersion"]}")
    api("io.mockk:mockk-android:${rootProject.extra["mockkAndroidVersion"]}")
    api("com.google.dagger:hilt-android-testing:${rootProject.extra["hiltTestingVersion"]}")
}

configurations.all {
    resolutionStrategy {
        force("org.objenesis:objenesis:3.2")
    }
}