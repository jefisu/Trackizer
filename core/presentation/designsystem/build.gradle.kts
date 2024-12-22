plugins {
    alias(libs.plugins.trackizer.android.library)
    alias(libs.plugins.trackizer.android.library.compose)
}

android {
    namespace = "com.jefisu.designsystem"
}

dependencies {
    implementation(projects.core.presentation.ui)
    implementation(projects.core.domain)
}