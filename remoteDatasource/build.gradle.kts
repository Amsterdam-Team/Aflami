import java.util.Properties

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

val bearerToken: String = properties.getProperty("bearerToken") ?: ""
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
        buildConfigField("String", "BEARER_TOKEN", "\"$bearerToken\"")
        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
    }
}

dependencies {
    implementation(project(":repository"))

    // Ktor client setup
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)

    // JSON serialization
    implementation(libs.kotlinx.serialization.json)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Core utils
    implementation(libs.androidx.core.ktx)

    // Unit testing
    testImplementation(libs.junit)
}
