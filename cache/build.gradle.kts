plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    androidLibrary {
        namespace = "com.guilherme.marvelcharacters.cache"
        compileSdk = 35
        minSdk = 24

        withHostTest {}
    }

    val xcfName = "cacheKit"

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
                implementation(project(":core:model"))
                implementation(libs.kotlin.stdlib)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        androidMain {
            dependencies {
                implementation(project(":core:testing"))
                implementation(libs.core.ktx)
                implementation(libs.koin.android)
                implementation(libs.datastore.preferences)
            }
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}

//apply(from = "$rootDir/tools/jacoco/android.gradle")
//
//android {
//    namespace = "com.guilherme.marvelcharacters.cache"
//    compileSdk = rootProject.extra["compileSdkVersion"] as Int
//
//    defaultConfig {
//        minSdk = rootProject.extra["minSdkVersion"] as Int
//    }
//    compileOptions {
//        sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
//        targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
//    }
//    kotlinOptions {
//        jvmTarget = rootProject.extra["jvmTargetVersion"].toString()
//    }
//}
//
//dependencies {
//    implementation(project(":core:model"))
//
//    implementation(libs.core.ktx)
//    implementation(libs.datastore.preferences)
//
//    implementation(libs.room.runtime)
//    implementation(libs.sqlite.bundled)
//    ksp(libs.room.compiler)
//
//    implementation(platform(libs.koin.bom))
//    implementation(libs.koin.android)
//
//    testImplementation(project(":core:testing"))
//}