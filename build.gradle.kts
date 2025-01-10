// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("com.android.application") version "7.3.1" apply false
    kotlin("android") version "1.7.20" apply false
    kotlin("plugin.serialization") version "1.7.20" apply false
    id("jacoco")
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("io.gitlab.arturbosch.detekt") version "1.8.0"
    id("com.github.kt3k.coveralls") version "2.12.0"
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        android.set(true)
        reporters {
            reporter(ReporterType.JSON)
        }
    }

    detekt {
        config = files("${rootProject.rootDir}/tools/detekt/detekt.yml")
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt/detekt_$name.html")
            }
            xml {
                enabled = false
            }
            txt {
                enabled = false
            }
        }
    }
}

apply(from = "dependencies.gradle")
apply(from = "project.gradle")

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
