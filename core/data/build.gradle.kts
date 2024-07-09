plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.jefisu.data"

    buildFeatures {
        compose = false
    }
}

dependencies {
    implementation(libs.androidx.datastore.preferences)

    implementation(projects.core.common)
}