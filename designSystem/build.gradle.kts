plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.amsterdam.designsystem"
}

dependencies {
    jetpackMaterial3Dependencies()
    composeBomDependencies()
    kotlinExtensionDependencies()
    androidPreviewDependencies()
}

private fun DependencyHandlerScope.jetpackMaterial3Dependencies() {
    implementation(libs.androidx.material3)
}

private fun DependencyHandlerScope.composeBomDependencies() {
    implementation(platform(libs.androidx.compose.bom))
}

private fun DependencyHandlerScope.kotlinExtensionDependencies() {
    implementation(libs.androidx.core.ktx)
}

private fun DependencyHandlerScope.androidPreviewDependencies() {
    implementation(libs.androidx.ui.tooling.preview.android)
    debugImplementation(libs.androidx.ui.tooling)
}