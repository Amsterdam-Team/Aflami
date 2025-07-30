plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.amsterdam.imageviewer"
}

dependencies {
    tensorFlowDependencies()
    coilDependencies()
    kotlinExtensionsDependencies()
    composeBomDependencies()
    firebaseDependencies()
    androidxStartupDependencies()
}

private fun DependencyHandlerScope.tensorFlowDependencies() {
    implementation(libs.tensorflow.lite.task.vision)
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