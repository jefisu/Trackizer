plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.jefisu.common"
}

dependencies {
    api(libs.androidx.core.ktx)
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.material3)
    debugApi(libs.androidx.ui.tooling)
    api(libs.androidx.material.icons.extended)

    api(libs.navigation.compose)
    api(libs.kotlinx.serialization.json)

    api(platform(libs.koin.bom))
    api(libs.koin.core)
    api(libs.koin.androidx.compose)

    api(libs.firebase.auth)

    testApi(libs.junit)
    androidTestApi(libs.androidx.junit)
    androidTestApi(libs.androidx.espresso.core)
    androidTestApi(platform(libs.androidx.compose.bom))
    androidTestApi(libs.androidx.ui.test.junit4)
    debugApi(libs.androidx.ui.tooling)
    debugApi(libs.androidx.ui.test.manifest)
}
