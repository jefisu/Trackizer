plugins {
    alias(libs.plugins.trackizer.android.library)
    alias(libs.plugins.trackizer.android.hilt)
    alias(libs.plugins.trackizer.android.firebase)
    alias(libs.plugins.trackizer.android.realm)
}

android {
    namespace = "com.jefisu.di"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.presentation.ui)
}
