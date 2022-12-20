plugins {
    id("com.android.library")
    id("dagger.hilt.android.plugin")
    kotlin("android")
    kotlin("kapt")
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters.feature.favorites"
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

    implementation("androidx.core:core-ktx:${rootProject.extra["coreVersion"]}")
    implementation("androidx.fragment:fragment-ktx:${rootProject.extra["fragmentVersion"]}")
    debugImplementation("androidx.fragment:fragment-testing:${rootProject.extra["fragmentVersion"]}") {
        exclude(group = "androidx.test", module = "monitor")
    }
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:${rootProject.extra["lifecycleVersion"]}")
    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigationVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${rootProject.extra["coroutinesVersion"]}")

    implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltVersion"]}")
    kapt("com.google.dagger:hilt-compiler:${rootProject.extra["hiltVersion"]}")

    implementation("com.github.bumptech.glide:glide:${rootProject.extra["glideVersion"]}")
    kapt("com.github.bumptech.glide:compiler:${rootProject.extra["glideVersion"]}")

    kaptAndroidTest("com.google.dagger:hilt-android-compiler:${rootProject.extra["hiltTestingVersion"]}")

    testImplementation(project(":core:testing"))
    androidTestImplementation(project(":core:testing"))
    androidTestUtil("androidx.test:orchestrator:${rootProject.extra["orchestratorVersion"]}")
}

configurations.all {
    resolutionStrategy {
        force("org.objenesis:objenesis:3.2")
    }
}