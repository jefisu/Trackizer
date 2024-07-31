plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.jefisu.data"

    buildFeatures {
        compose = false
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.firebase.firestore)

    implementation(projects.core.common)
    implementation(projects.core.domain)

    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
