import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val movieSignUp: String = properties.getProperty("movieSignUp") ?: ""
val movieResetPassword: String = properties.getProperty("movieResetPassword") ?: ""

plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.amsterdam.viewmodel"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "MOVIE_SIGN_UP_URL",
            movieSignUp
        )
        buildConfigField(
            "String",
            "MOVIE_RESET_PASSWORD_URL",
            movieResetPassword
        )
    }
}

dependencies {
    api(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.kotlinx.datetime)

    // page 3
    implementation(libs.androidx.paging.runtime)

    //hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // mockk
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)

    // junit 5
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    //truth
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
    testImplementation("app.cash.turbine:turbine:1.1.0")

}