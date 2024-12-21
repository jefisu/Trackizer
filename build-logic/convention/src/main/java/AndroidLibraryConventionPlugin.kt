import com.android.build.gradle.LibraryExtension
import com.jefisu.trackizer.build.getPluginId
import com.jefisu.trackizer.build_logic.convention.configureKotlinAndroid
import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply(libs.getPluginId("kotlin-serialization"))
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
            }

            dependencies {
                implementation(libs.findLibrary("kotlinx-serialization-json").get())
                implementation(libs.findLibrary("androidx-worker").get())
            }
        }
    }
}
