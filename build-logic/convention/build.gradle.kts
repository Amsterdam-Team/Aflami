plugins {
    `kotlin-dsl`
}

group = "com.amsterdam.convention"

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(libs.android.tools.build.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("AndroidLibraryCommonPlugin") {
            id = "com.thechance.plugin"
            implementationClass = "AndroidLibraryCommonPlugin"
        }
    }
}