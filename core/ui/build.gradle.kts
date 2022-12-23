plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.guilherme.marvelcharacters.core.ui"
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
    implementation("androidx.core:core-ktx:${rootProject.extra["coreVersion"]}")
    api("com.google.android.material:material:${rootProject.extra["materialDesignVersion"]}")
    api("com.google.accompanist:accompanist-themeadapter-material:${rootProject.extra["materialThemeAdapterVersion"]}")
}