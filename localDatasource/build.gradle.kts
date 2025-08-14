plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kover)
    alias(libs.plugins.android.junit5)
}

android {
    namespace = libs.versions.namespaceLocalDatasource.get()

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunnerArguments["runnerBuilder"] =
            "de.mannodermaus.junit5.AndroidJUnit5Builder"
    }
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    modulesDependencies()
    roomDependencies()
    androidTestDependencies()
    coroutineDependencies()
    dateTimeDependencies()
    unitTestDependencies()
    datastoreDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    implementation(projects.repository)
}

private fun DependencyHandlerScope.roomDependencies() {
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}

private fun DependencyHandlerScope.androidTestDependencies() {
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.android.test.core)
    androidTestRuntimeOnly(libs.android.test.runner)
    androidTestImplementation(libs.truth)
    androidTestImplementation(libs.kotlinx.coroutines.test)

}

private fun DependencyHandlerScope.coroutineDependencies() {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

private fun DependencyHandlerScope.dateTimeDependencies() {
    implementation(libs.kotlinx.datetime)
}

private fun DependencyHandlerScope.unitTestDependencies() {
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.kotlinx.coroutines.test)
}

private fun DependencyHandlerScope.datastoreDependencies() {
    implementation(libs.androidx.datastore.preferences)

    implementation(libs.javax.inject)
}

kover.reports{
    filters.excludes {
        androidGeneratedClasses()
        packages(
            "*.converter",
            "*.daos"
        )
        classes(
            "*AflamiDatabase*",
            "*${'$'}${'$'}inlined${'$'}*"
        )
    }

    verify.rule {
        minBound(80)
    }
}