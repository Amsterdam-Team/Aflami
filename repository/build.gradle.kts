plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.amsterdam.repository"
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }
}

dependencies {

    modulesDependencies()
    roomDependencies()
    testDependencies()
    jsonSerializationDependencies()
    coroutinesDependencies()
    kotlinExtensionsDependencies()
    kotlinDateTimeDependencies()
    appCompactDependencies()
    injectDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    api(project(":domain"))

}

private fun DependencyHandlerScope.roomDependencies() {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}

private fun DependencyHandlerScope.testDependencies() {
    testImplementation(libs.junit.jupiter)
    testImplementation(kotlin("test"))
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
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

private fun DependencyHandlerScope.kotlinDateTimeDependencies() {
    implementation(libs.kotlinx.datetime)
}

private fun DependencyHandlerScope.appCompactDependencies() {
    implementation(libs.androidx.appcompat)
}

private fun DependencyHandlerScope.injectDependencies() {
    implementation(libs.javax.inject)
}