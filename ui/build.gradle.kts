plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.amsterdam.ui"
}

dependencies {
    modulesDependencies()
    navigationDependencies()
    lifeCycleDependencies()
    sifrDependencies()
    pagingDependencies()
    kotlinExtensionsDependencies()
    appCompactDependencies()
    previewDependencies()
    composeDependencies()
    uiGraphicsDependencies()
    hiltDependencies()
    kotlinxDateTime()
}

private fun DependencyHandlerScope.modulesDependencies() {
    implementation(project(":designSystem"))
    implementation(project(":viewModel"))
    implementation(project(":imageViewer"))
}

private fun DependencyHandlerScope.navigationDependencies() {
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.android)
}

private fun DependencyHandlerScope.lifeCycleDependencies() {
    implementation(libs.androidx.lifecycle.runtime.ktx)
}

private fun DependencyHandlerScope.sifrDependencies() {
    implementation(libs.sifr.shaded)
}

private fun DependencyHandlerScope.pagingDependencies() {
    implementation(libs.androidx.paging.compose)
}

private fun DependencyHandlerScope.kotlinExtensionsDependencies() {
    implementation(libs.androidx.core.ktx)
}

private fun DependencyHandlerScope.appCompactDependencies() {
    implementation(libs.androidx.appcompat)
}

private fun DependencyHandlerScope.previewDependencies() {
    implementation(libs.androidx.ui.tooling.preview.android)
}

private fun DependencyHandlerScope.composeDependencies() {
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.foundation)
}

private fun DependencyHandlerScope.uiGraphicsDependencies() {
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
}

private fun DependencyHandlerScope.hiltDependencies() {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation (libs.androidx.hilt.navigation.compose)
}

private fun DependencyHandlerScope.kotlinxDateTime() {
    implementation(libs.kotlinx.datetime)
}

private fun DependencyHandlerScope.coilDependencies() {
    implementation(libs.coil.compose)
}
