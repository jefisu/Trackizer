plugins {
    alias(libs.plugins.trackizer.android.library)
    alias(libs.plugins.trackizer.android.hilt)
    alias(libs.plugins.trackizer.android.firebase)
    alias(libs.plugins.trackizer.android.realm)
    alias(libs.plugins.trackizer.android.ktor.client)
    alias(libs.plugins.secret.gradle)
}

android {
    namespace = "com.jefisu.data"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.androidx.appcompat)
}