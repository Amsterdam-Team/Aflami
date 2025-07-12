plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.example.repository"
    defaultConfig {
        testInstrumentationRunnerArguments["runnerBuilder"] = "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }
}

dependencies {
    api(project(":domain"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    // Room
    implementation(libs.androidx.room.runtime)
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
