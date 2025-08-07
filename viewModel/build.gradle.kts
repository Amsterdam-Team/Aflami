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
    modulesDependencies()
    coroutinesDependencies()
    kotlinExtensionsDependencies()
    appCompactDependencies()
    lifeCycleDependencies()
    dateTimeDependencies()
    pagingDependencies()
    testDependencies()
    hiltDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    api(project(":domain"))
}

private fun DependencyHandlerScope.coroutinesDependencies() {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

private fun DependencyHandlerScope.kotlinExtensionsDependencies() {
    implementation(libs.androidx.core.ktx)
}

private fun DependencyHandlerScope.appCompactDependencies() {
    implementation(libs.androidx.appcompat)
}

private fun DependencyHandlerScope.lifeCycleDependencies() {
    implementation(libs.androidx.lifecycle.runtime.ktx)
}

private fun DependencyHandlerScope.dateTimeDependencies() {
    implementation(libs.kotlinx.datetime)
}
private fun DependencyHandlerScope.pagingDependencies() {
    implementation(libs.androidx.paging.runtime)
}

private fun DependencyHandlerScope.testDependencies() {
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
    testImplementation("app.cash.turbine:turbine:1.1.0")
}

private fun DependencyHandlerScope.hiltDependencies() {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}