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
}

android {
    namespace = "com.example.remotedatasource"

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
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Or the latest stable version
    implementation("com.squareup.okhttp3:okhttp:4.12.0") // Or the latest stable version
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // For logging, similar to Ktor's logging

    // Converter for kotlinx.serialization with Retrofit
    // This adapter allows Retrofit to use kotlinx.serialization for JSON parsing
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")

    // You'll also need the kotlinx-serialization-json if you don't have it already
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Or the latest stable version
    // Unit Testing
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}