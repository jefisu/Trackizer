plugins {
    alias(libs.plugins.trackizer.android.library)
    alias(libs.plugins.trackizer.android.library.compose)
}

android {
    namespace = "com.jefisu.ui"
}

dependencies {
    implementation(projects.core.domain)
}