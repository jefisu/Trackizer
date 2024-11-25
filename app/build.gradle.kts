import com.jefisu.trackizer.build_logic.convention.implementation
import java.util.Properties

plugins {
    alias(libs.plugins.trackizer.android.application)
    alias(libs.plugins.trackizer.android.application.compose)
    alias(libs.plugins.trackizer.android.hilt)
    alias(libs.plugins.trackizer.android.firebase)
    alias(libs.plugins.secret.gradle)
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

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
    implementation(projects.feature.addSubscription)
    implementation(projects.feature.subscriptionInfo)
    implementation(projects.feature.settings)

    // Modules
    implementation(projects.core.data)
    implementation(projects.core.di)
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

fun getSigningConfigProperty(propertyName: String): String {
    val propertiesFile = rootProject.file("local.properties")
    val properties = Properties().apply {
        load(propertiesFile.inputStream())
    }
    return properties.getProperty(propertyName)
}
