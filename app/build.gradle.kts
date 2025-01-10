plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

apply(from = "$rootDir/tools/jacoco/android.gradle")

android {
    namespace = "com.guilherme.marvelcharacters"
    compileSdk = rootProject.extra["compileSdkVersion"] as Int
    defaultConfig {
        applicationId = "com.guilherme.marvelcharacters"
        minSdk = rootProject.extra["minSdkVersion"] as Int
        targetSdk = rootProject.extra["targetSdkVersion"] as Int
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("debug") {
            enableUnitTestCoverage = false
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
        targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
    }

    kotlinOptions {
        jvmTarget = rootProject.extra["jvmTargetVersion"].toString()
    }

    kapt {
        correctErrorTypes = true
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(baseDir = "libs") { include("*.jar") })
    implementation(project(":cache"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":remote"))
    implementation(project(":feature:home"))
    implementation(project(":feature:favorites"))
    implementation(project(":feature:detail"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation("androidx.core:core-ktx:${rootProject.extra["coreVersion"]}")

    implementation("androidx.navigation:navigation-fragment-ktx:${rootProject.extra["navigationVersion"]}")
    implementation("androidx.navigation:navigation-ui-ktx:${rootProject.extra["navigationVersion"]}")
}