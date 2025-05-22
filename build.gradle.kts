// Top-level build file where you can add configuration options common to all sub-projects/modules.
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.coveralls)
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
    delete(rootProject.layout.buildDirectory)
}
