import com.jefisu.trackizer.build_logic.convention.implementation
import com.jefisu.trackizer.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            dependencies {
                implementation(platform(libs.findLibrary("firebase-bom").get()))
                implementation(libs.findBundle("firebase").get())
                implementation(libs.findLibrary("googleid").get())
                implementation(libs.findLibrary("androidx-credentials").get())
                implementation(libs.findLibrary("facebook-login").get())
            }
        }
    }
}
