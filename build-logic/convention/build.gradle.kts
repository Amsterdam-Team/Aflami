plugins {
    `kotlin-dsl`
}

group = "com.example.convention"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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