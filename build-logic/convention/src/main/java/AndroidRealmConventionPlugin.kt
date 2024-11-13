import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidRealmConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.realm.kotlin")
            dependencies {
                implementation(libs.findLibrary("realm-library").get())
            }
        }
    }
}