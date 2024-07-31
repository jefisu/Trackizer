plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.jefisu.domain"

    buildFeatures {
        compose = false
    }
}

dependencies {
    implementation(projects.core.common)
}
