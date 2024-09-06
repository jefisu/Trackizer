pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

rootProject.name = "Trackizer"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:presentation:designsystem")
include(":feature:welcome")
include(":feature:auth")
include(":core:domain")
include(":core:data")
include(":feature:home")
include(":feature:spending-budgets")
include(":feature:calendar")
include(":feature:credit-cards")
include(":core:presentation:ui")
include(":feature:add-subscription")
