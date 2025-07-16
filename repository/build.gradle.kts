import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val baseImageUrl: String = properties.getProperty("baseImageUrl") ?: ""

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.junit5)
}

android {
    namespace = "com.example.repository"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"

        buildConfigField(
            "String",
            "BASE_IMAGE_URL",
            baseImageUrl
        )
    }
}

dependencies {
    api(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.core)
    // Room
    implementation(libs.androidx.room.runtime)
    testImplementation(libs.junit.jupiter)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Date and Time
    implementation(libs.kotlinx.datetime)
    // kotlinx.serialization
    implementation(libs.kotlinx.serialization.json)
    // junit 5
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    //truth
    testImplementation (libs.truth)
    //mockk
    testImplementation (libs.mockk)
    testImplementation (libs.kotlinx.coroutines.test)

}
