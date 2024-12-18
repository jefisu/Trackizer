import com.android.build.api.dsl.ApplicationExtension
import com.jefisu.trackizer.build_logic.convention.configureAndroidCompose
import com.jefisu.trackizer.build_logic.convention.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.application")
            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)

            dependencies {
                implementation(project(":core:presentation:designsystem"))
            }
        }
    }
}
