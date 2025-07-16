plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.aflami.custom.plugin)
}

android {
    namespace = "com.example.designsystem"
}

dependencies {
    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.ui.tooling.preview.android)
    debugImplementation(libs.androidx.ui.tooling)
    api(project(":imageViewer"))
}
