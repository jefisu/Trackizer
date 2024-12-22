import com.android.build.api.dsl.ApplicationExtension
import com.jefisu.trackizer.build_logic.convention.configureAndroidCompose
import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("trackizer.android.application")

            extensions.configure<ApplicationExtension> {
                configureAndroidCompose(this)
            }

            dependencies {
                implementation(libs.findBundle("app-compose").get())
                implementation(project(":core:presentation:designsystem"))
            }
        }
    }
}
