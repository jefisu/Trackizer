import com.android.build.gradle.LibraryExtension
import com.jefisu.trackizer.build_logic.convention.configureKotlinAndroid
import com.jefisu.trackizer.build_logic.convention.libs
import com.jefisu.trackizer.build_logic.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk =
                    libs.findVersion("compileTargetSdk").get().displayName.toInt()
            }

            dependencies {
                testImplementation(kotlin("test"))
            }
        }
    }
}
