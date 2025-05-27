import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidLibrary {
        namespace = "com.guilherme.marvelcharacters.remote"
        compileSdk = 35
        minSdk = 24

        withHostTest {}
    }

    val xcfName = "remoteKit"

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
                implementation(libs.kotlinx.serialization.json)

                implementation(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)

                implementation(libs.okio)

                implementation(libs.bundles.ktor)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.ktor.client.mock)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.koin.android)
                implementation(libs.ktor.client.okhttp)
            }
        }

        getByName("androidHostTest") {
            dependencies {
                implementation(project(":core:testing"))
                implementation(libs.mockwebserver)
            }
        }
    }
}

buildkonfig {
    packageName = "com.guilherme.marvelcharacters.remote"
    defaultConfigs {
        val keystorePropertiesFile = rootProject.file("keystore.properties")
        val keystoreProperties = Properties()

        keystoreProperties.load(FileInputStream(keystorePropertiesFile))

        buildConfigField(STRING, "MARVEL_KEY", "\"${keystoreProperties["MARVEL_KEY"]}\"")
        buildConfigField(STRING, "MARVEL_PRIVATE_KEY", "\"${keystoreProperties["MARVEL_PRIVATE_KEY"]}\"")
    }
}
