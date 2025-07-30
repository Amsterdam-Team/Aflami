import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

class AndroidLibraryCommonPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

        applyRequiredPlugins(libs)
        configureKotlinCompiler()
        configureAndroidLibrary(libs)
    }

    private fun Project.applyRequiredPlugins(libs: VersionCatalog) {
        val androidLibraryId = libs.findPlugin("android.library")
            .orElseThrow { error("Missing plugin: android.library in libs.versions.toml") }
            .get().pluginId

        val kotlinAndroidId = libs.findPlugin("kotlin.android")
            .orElseThrow { error("Missing plugin: kotlin.android in libs.versions.toml") }
            .get().pluginId

        pluginManager.apply(androidLibraryId)
        pluginManager.apply(kotlinAndroidId)
    }

    private fun Project.configureKotlinCompiler() {
        extensions.configure<KotlinAndroidProjectExtension> {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
            }
        }
    }

    private fun Project.configureAndroidLibrary(libs: VersionCatalog) {
        val compileSdk = libs.findVersion("compileSdk").get().requiredVersion.toInt()
        val minSdk = libs.findVersion("minSdk").get().requiredVersion.toInt()
        val jvmVersion = libs.findVersion("jvmTarget").get().requiredVersion

        extensions.configure<LibraryExtension> {
            this.compileSdk = compileSdk

            defaultConfig {
                this.minSdk = minSdk
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }
            }

            compileOptions {
                sourceCompatibility = JavaVersion.toVersion(jvmVersion)
                targetCompatibility = JavaVersion.toVersion(jvmVersion)
            }
        }
    }
}