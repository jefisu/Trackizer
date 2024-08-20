plugins {
    alias(libs.plugins.trackizer.android.library.compose)
    alias(libs.plugins.trackizer.android.feature)
    alias(libs.plugins.trackizer.android.firebase)
    alias(libs.plugins.secret.gradle)
}

android {
    namespace = "com.jefisu.auth"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
}
