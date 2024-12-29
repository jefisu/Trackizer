import com.android.build.api.dsl.ApplicationExtension
import com.jefisu.trackizer.build.getPluginId
import com.jefisu.trackizer.build_logic.convention.androidTestImplementation
import com.jefisu.trackizer.build_logic.convention.configureKotlinAndroid
import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import com.jefisu.trackizer.build_logic.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("com.android.application")
            apply(libs.getPluginId("secret.gradle"))
            apply("trackizer.android.hilt")
            apply("trackizer.android.firebase")
        }

        extensions.configure<ApplicationExtension> {
            configureKotlinAndroid(this)
            configureApplication()
        }

        dependencies {
            implementation(libs.findBundle("app").get())
            testImplementation(libs.findBundle("app-test").get())
            androidTestImplementation(libs.findBundle("app-android-test").get())
        }
    }
}

private fun ApplicationExtension.configureApplication() {
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    defaultConfig {
        targetSdk = 35

        versionCode = 1
        versionName = "0.0.1"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            buildConfig = true
        }
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