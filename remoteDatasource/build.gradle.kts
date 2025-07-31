import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
var bearerToken: String = properties.getProperty("bearerToken") ?: ""
val baseUrl: String = properties.getProperty("baseUrl") ?: ""

plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kotlin.serialization)
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
    modulesDependencies()
    retrofitDependencies()
    unitTestDependencies()
    kotlinExtensionsDependencies()
    jsonSerializationDependencies()
    coroutinesDependencies()
    kotlinExtensionsDependencies()
    injectDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    implementation(project(":repository"))
}

private fun DependencyHandlerScope.retrofitDependencies() {
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.retrofit2.kotlinx.serialization.converter)
}

private fun DependencyHandlerScope.unitTestDependencies() {
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
}

private fun DependencyHandlerScope.jsonSerializationDependencies() {
    implementation(libs.kotlinx.serialization.json)
}

private fun DependencyHandlerScope.coroutinesDependencies() {
    implementation(libs.kotlinx.coroutines.core)
}

private fun DependencyHandlerScope.kotlinExtensionsDependencies() {
    implementation(libs.androidx.core.ktx)
}
private fun DependencyHandlerScope.injectDependencies() {
    implementation(libs.javax.inject)
}