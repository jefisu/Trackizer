import com.jefisu.trackizer.build_logic.convention.implementation

plugins {
    alias(libs.plugins.trackizer.android.application)
    alias(libs.plugins.trackizer.android.application.compose)
    alias(libs.plugins.trackizer.android.hilt)
    alias(libs.plugins.trackizer.android.firebase)
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
    // Feature
    implementation(projects.feature.welcome)
    implementation(projects.feature.auth)
    implementation(projects.feature.home)
    implementation(projects.feature.spendingBudgets)
    implementation(projects.feature.calendar)
    implementation(projects.feature.creditCards)

    // Modules
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
}
