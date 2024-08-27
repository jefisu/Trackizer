plugins {
    alias(libs.plugins.trackizer.android.feature)
}

android {
    namespace = "com.jefisu.credit_cards"
}

dependencies {
    implementation(libs.creditcardhelper)
}
