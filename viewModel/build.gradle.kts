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
    implementation(project(":domain"))
    implementation(project(":entity"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation (libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.kotlinx.datetime)
    //mockk
    testImplementation (libs.mockk)
    testImplementation (libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)
    // junit 5
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.junit.jupiter.params)
    testImplementation(kotlin("test"))
    //truth
    testImplementation (libs.truth)
}