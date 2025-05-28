plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.compose.multiplatform)
}

kotlin {
    androidLibrary {
        namespace = "com.guilherme.marvelcharacters.core.ui"
        compileSdk = 35
        minSdk = 24
    }

    val xcfName = "coreUiKit"

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
                implementation(libs.kotlin.stdlib)
                implementation(compose.runtime)
                implementation(compose.foundation)
                api(compose.material3)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.components.uiToolingPreview)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.core.ktx)
                implementation(compose.preview)
            }
        }
    }
}
