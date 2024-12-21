package com.jefisu.trackizer.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.jefisu.trackizer.build.getPluginId
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    with(pluginManager) {
        apply(libs.getPluginId("compose-compiler"))
        apply(libs.getPluginId("kotlin-serialization"))
    }

    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))

            implementation(libs.findBundle("compose").get())
            debugImplementation(libs.findBundle("compose-debug").get())
        }
    }
}
