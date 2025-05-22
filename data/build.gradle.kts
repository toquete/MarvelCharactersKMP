plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters.data"
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
    implementation(project(":cache"))
    implementation(project(":remote"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    testImplementation(project(":core:testing"))
}