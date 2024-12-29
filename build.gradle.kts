import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.secret.gradle) apply false
    alias(libs.plugins.realm) apply false
}

allprojects {
    configureKtlint()
}

fun Project.configureKtlint() {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<KtlintExtension> {
        android.set(true)
        ignoreFailures.set(true)
        outputToConsole.set(true)
        outputColorName.set("RED")
        enableExperimentalRules.set(true)
        reporters {
            reporter(ReporterType.CHECKSTYLE)
            reporter(ReporterType.HTML)
        }
    }
}