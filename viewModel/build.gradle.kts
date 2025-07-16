plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.aflami.custom.plugin)
    id("de.mannodermaus.android-junit5")
}

android {
    namespace = "com.example.viewmodel"
}

dependencies {
    // Internal modules
    api(project(":domain"))

    // Core & Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // JUnit 5 Testing
    testImplementation(libs.junit.jupiter.api)
    testImplementation(libs.junit.jupiter.params)
    testRuntimeOnly(libs.junit.jupiter.engine)

    testImplementation(kotlin("test"))

    // Coroutine Test
    testImplementation(libs.kotlinx.coroutines.test)

    // Truth assertions
    testImplementation(libs.truth)

    // Mocking
    testImplementation(libs.mockk)
}
