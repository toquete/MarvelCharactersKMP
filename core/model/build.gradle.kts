plugins {
    id("java-library")
    id("kotlin")
}

apply(from = "$rootDir/tools/jacoco/kotlin.gradle")

java {
    sourceCompatibility = rootProject.extra["sourceCompatibilityVersion"] as JavaVersion
    targetCompatibility = rootProject.extra["targetCompatibilityVersion"] as JavaVersion
}