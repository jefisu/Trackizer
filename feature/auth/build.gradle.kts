plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secret.gradle)
}

android {
    namespace = "com.jefisu.auth"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.credentials)
    implementation(libs.googleid)
    implementation(libs.facebook.login)

    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.core.data)
}
