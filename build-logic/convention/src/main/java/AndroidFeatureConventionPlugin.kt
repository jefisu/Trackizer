import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("trackizer.android.library")
                apply("trackizer.android.hilt")
                apply("trackizer.android.library.compose")
                apply("org.jetbrains.kotlin.plugin.serialization")
            }

            dependencies {
                implementation(project(":core:presentation:designsystem"))
                implementation(project(":core:domain"))
                implementation(project(":core:data"))

                // Define common dependencies for feature modules
                implementation(libs.findLibrary("androidx-navigation-compose").get())
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
            }
        }
    }
}
