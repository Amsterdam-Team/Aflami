plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kotlin.compose)
}

android { namespace = libs.versions.namespaceImageViewer.get() }

dependencies {
    tensorFlowDependencies()
    coilDependencies()
    kotlinExtensionsDependencies()
    composeBomDependencies()
    firebaseDependencies()
    androidxStartupDependencies()
}

private fun DependencyHandlerScope.tensorFlowDependencies() {
    implementation(files("libs/tensorflow-lite.aar"))
    implementation(files("libs/tensorflowlite_support_java.aar"))
    implementation(files("libs/tensorflow-lite-task-vision-0.4.4.aar"))
    implementation(files("libs/tensorflow-lite-task-base-0.4.4.aar"))
}

private fun DependencyHandlerScope.coilDependencies() {
    implementation(libs.coil.compose)
}

private fun DependencyHandlerScope.kotlinExtensionsDependencies() {
    implementation(libs.androidx.core.ktx)
}

private fun DependencyHandlerScope.composeBomDependencies() {
    implementation(platform(libs.androidx.compose.bom))
}

private fun DependencyHandlerScope.firebaseDependencies() {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ml.modeldownloader)
}

private fun DependencyHandlerScope.androidxStartupDependencies() {
    implementation(libs.androidx.startup.runtime)
}