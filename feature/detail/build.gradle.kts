plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters.feature.detail"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int

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
    debugImplementation(libs.fragment.testing) {
        exclude(group = "androidx.test", module = "monitor")
    }
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.savedstate)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.glide)
    kapt(libs.glide.compiler)

    kaptAndroidTest(libs.hilt.android.compiler)

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestUtil(libs.orchestrator)
}

configurations.all {
    resolutionStrategy {
        force("org.objenesis:objenesis:3.2")
    }
}