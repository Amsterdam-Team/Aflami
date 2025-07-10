import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
var bearerToken: String = properties.getProperty("bearerToken")?:""


plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
   // alias(libs.plugins.aflami.custom.plugin)
}

android {
    namespace = "com.example.remotedatasource"
    compileSdk = 34
    buildFeatures {
        buildConfig = true

    }

    defaultConfig{
        buildConfigField(
            "String",
            "BEARER_TOKEN",
            bearerToken
        )
    }
}

dependencies {
    implementation(project(":repository"))
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
}