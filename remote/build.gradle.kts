import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt)
    alias(libs.plugins.kotlin.serialization)
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
    implementation(project(":core:common"))

    implementation(libs.retrofit)
    implementation(libs.okHttp)
    implementation(libs.kotlin.serialization.converter)
    implementation(libs.commons.codec)
    implementation(libs.kotlin.serialization)

    testImplementation(project(":core:testing"))
    testImplementation(libs.mockwebserver)
}