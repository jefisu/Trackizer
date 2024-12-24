plugins {
    alias(libs.plugins.trackizer.android.application.compose)
}

android {
    namespace = "com.jefisu.trackizer"

    defaultConfig {
        applicationId = "com.jefisu.trackizer"
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
    implementation(projects.feature.user)

    // Modules
    implementation(projects.core.data)
    implementation(projects.core.di)
    implementation(projects.core.domain)
    implementation(projects.core.presentation.ui)
}