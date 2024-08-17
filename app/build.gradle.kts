import com.jefisu.trackizer.build_logic.convention.implementation

plugins {
    alias(libs.plugins.trackizer.android.application)
    alias(libs.plugins.trackizer.android.application.compose)
    alias(libs.plugins.trackizer.android.hilt)
}

android {
    namespace = "com.jefisu.trackizer"

    defaultConfig {
        applicationId = "com.jefisu.trackizer"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
}
