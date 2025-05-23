plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters.feature.favorites"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int

        testInstrumentationRunner = "com.guilherme.marvelcharacters.core.testing.CustomAndroidTestRunner"
    }
    compileOptions {
        sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
    }
    kotlinOptions {
        jvmTarget = rootProject.extra["jvmTargetVersion"].toString()
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    implementation(libs.core.ktx)
    implementation(libs.fragment.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestUtil(libs.orchestrator)
}
