import com.jefisu.trackizer.build_logic.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("trackizer.android.library.compose")
                apply("trackizer.android.hilt")
            }

            dependencies {
                implementation(project(":core:presentation:designsystem"))
                implementation(project(":core:presentation:ui"))
                implementation(project(":core:domain"))
            }
        }
    }
}
