import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
var bearerToken: String = properties.getProperty("bearerToken")?:""
val baseUrl: String = properties.getProperty("baseUrl") ?: ""

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.aflami.custom.plugin)
}

android {
    namespace = "com.amsterdam.remotedatasource"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig{
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
    implementation (libs.ktor.client.serialization)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
}