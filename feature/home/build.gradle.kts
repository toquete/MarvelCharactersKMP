plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.compose.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mokkery)
}

kotlin {
    androidLibrary {
        namespace = "com.guilherme.marvelcharacters.feature.home"
        compileSdk = 35
        minSdk = 24

        withHostTest {  }
    }

    val xcfName = "homeFeatureKit"

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
        getByName("androidHostTest") {
            dependencies {
                implementation(project(":core:testing"))
            }
        }
    }
}
