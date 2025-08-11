plugins {
    alias(libs.plugins.android.java.library)
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kover)
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTarget.get())
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    modulesDependencies()
    coroutinesDependencies()
    kotlinDateTimeDependencies()
    testDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    api(projects.entity)
}

private fun DependencyHandlerScope.coroutinesDependencies() {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

private fun DependencyHandlerScope.kotlinDateTimeDependencies() {
    implementation(libs.kotlinx.datetime)
}

private fun DependencyHandlerScope.testDependencies() {
    testImplementation(libs.truth)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}