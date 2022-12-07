pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}
rootProject.name = "Marvel Characters"
include(":app")
include(":cache")
include(":data")
include(":remote")
include(":domain")
include(":core:model")
include(":core:testing")
include(":core:ui")
include(":feature:detail")
include(":feature:favorites")
