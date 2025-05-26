plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters.feature.detail"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        compose = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:ui"))

    implementation(libs.core.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.coroutines.android)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.navigation.compose)

    implementation(libs.koin.androidx.compose)

    implementation(libs.kotlinx.serialization.json)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestUtil(libs.orchestrator)
}
