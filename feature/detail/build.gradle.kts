plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.compose.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidLibrary {
        namespace = "com.guilherme.marvelcharacters.feature.detail"
        compileSdk = 35
        minSdk = 24
    }

    val xcfName = "detailFeatureKit"

    iosX64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = xcfName
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":domain"))
                implementation(project(":core:model"))
                implementation(project(":core:ui"))
                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.androidx.navigation.compose)
                implementation(libs.androidx.lifecycle.viewmodel)
                implementation(libs.androidx.lifecycle.viewmodel.savedstate)
                implementation(libs.androidx.lifecycle.runtime.compose)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose.viewmodel.navigation)
            }
        }
    }
}

//apply(from = "$rootDir/tools/jacoco/android.gradle")
//
//android {
//    namespace = "com.guilherme.marvelcharacters.feature.detail"
//    compileSdk = rootProject.extra["compileSdkVersion"] as Int
//
//    defaultConfig {
//        minSdk = rootProject.extra["minSdkVersion"] as Int
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//    compileOptions {
//        sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
//        targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
//    }
//    kotlinOptions {
//        jvmTarget = rootProject.extra["jvmTargetVersion"].toString()
//    }
//    testOptions {
//        execution = "ANDROIDX_TEST_ORCHESTRATOR"
//    }
//    buildFeatures {
//        compose = true
//    }
//}
//
//dependencies {
//    implementation(project(":domain"))
//    implementation(project(":core:model"))
//    implementation(project(":core:ui"))
//
//    implementation(libs.core.ktx)
//    implementation(libs.lifecycle.viewmodel.ktx)
//    implementation(libs.lifecycle.runtime.ktx)
//    implementation(libs.lifecycle.viewmodel.savedstate)
//    implementation(libs.lifecycle.viewmodel.compose)
//    implementation(libs.kotlinx.coroutines.android)
//
//    implementation(platform(libs.androidx.compose.bom))
//    implementation(libs.androidx.compose.material)
//    implementation(libs.androidx.compose.material.icons.extended)
//    implementation(libs.androidx.compose.ui.tooling.preview)
//    debugImplementation(libs.androidx.compose.ui.tooling)
//    implementation(libs.navigation.compose)
//
//    implementation(platform(libs.koin.bom))
//    implementation(libs.koin.android)
//    implementation(libs.koin.androidx.compose)
//
//    implementation(libs.kotlinx.serialization.json)
//
//    testImplementation(project(":core:testing"))
//    androidTestImplementation(project(":core:testing"))
//    androidTestUtil(libs.orchestrator)
//}
