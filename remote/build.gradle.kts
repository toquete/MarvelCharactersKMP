import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    kotlin("plugin.serialization")
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters.remote"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int

    defaultConfig {
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int

        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()

        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        buildConfigField("String", "MARVEL_KEY", "\"${keystoreProperties["MARVEL_KEY"]}\"")
        buildConfigField("String", "MARVEL_PRIVATE_KEY", "\"${keystoreProperties["MARVEL_PRIVATE_KEY"]}\"")
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
    implementation(project(":core:model"))

    implementation("com.google.dagger:hilt-android:${rootProject.extra["hiltVersion"]}")
    kapt("com.google.dagger:hilt-compiler:${rootProject.extra["hiltVersion"]}")

    implementation("com.squareup.retrofit2:retrofit:${rootProject.extra["retrofitVersion"]}")
    implementation("com.squareup.okhttp3:okhttp:${rootProject.extra["okHttpVersion"]}")
    implementation(
        "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:${rootProject.extra["kotlinSerializationConverterVersion"]}"
    )
    implementation("commons-codec:commons-codec:${rootProject.extra["commonsCodecVersion"]}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${rootProject.extra["kotlinSerializationVersion"]}")

    testImplementation(project(":core:testing"))
    testImplementation("com.squareup.okhttp3:mockwebserver:${rootProject.extra["mockWebServerVersion"]}")
}