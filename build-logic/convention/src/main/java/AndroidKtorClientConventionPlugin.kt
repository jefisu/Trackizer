import com.jefisu.trackizer.build.getPluginId
import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKtorClientConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.getPluginId("kotlin-serialization"))
        }

        dependencies {
            implementation(libs.findBundle("ktor").get())
        }
    }
}