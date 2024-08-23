plugins {
    alias(libs.plugins.trackizer.android.library)
    alias(libs.plugins.trackizer.android.hilt)
    alias(libs.plugins.trackizer.android.firebase)
}

android {
    namespace = "com.jefisu.data"
}

dependencies {
    implementation(projects.core.domain)
}
