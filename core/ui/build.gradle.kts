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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.extra["kotlinCompilerExtensionVersion"].toString()
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2022.12.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.core:core-ktx:${rootProject.extra["coreVersion"]}")
    api("com.google.android.material:material:${rootProject.extra["materialDesignVersion"]}")
    api("com.google.accompanist:accompanist-themeadapter-material:${rootProject.extra["materialThemeAdapterVersion"]}")

    api("androidx.compose.material:material")
    api("androidx.compose.material:material-icons-extended")
    api("androidx.compose.ui:ui-tooling-preview")
    debugApi("androidx.compose.ui:ui-tooling")
    api("androidx.activity:activity-compose:${rootProject.extra["activityVersion"]}")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycleVersion"]}")
}