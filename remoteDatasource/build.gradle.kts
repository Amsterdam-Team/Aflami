import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
var bearerToken: String = properties.getProperty("bearerToken") ?: ""
val baseUrl: String = properties.getProperty("baseUrl") ?: ""

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kover)
    alias(libs.plugins.android.junit5)
}

android {
    namespace = "com.amsterdam.remotedatasource"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "BEARER_TOKEN",
            bearerToken

        )

        buildConfigField(
            "String",
            "BASE_URL",
            baseUrl

        )
    }
}

dependencies {
    implementation(project(":repository"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.kotlinx.serialization.json)
    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))

    implementation(libs.javax.inject)
}