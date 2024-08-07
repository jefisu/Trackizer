plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.jefisu.spending_budgets"
}

dependencies {

    implementation(projects.core.ui)
    implementation(projects.core.common)
    implementation(projects.core.domain)
}
