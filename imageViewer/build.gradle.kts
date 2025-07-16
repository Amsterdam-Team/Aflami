plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.aflami.custom.plugin)
}

android {
    namespace = "com.example.imageviewer"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material3)
    implementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.ui.tooling.preview.android)
    // Coil for image loading
    implementation(libs.coil.compose)

    // TensorFlow Lite for on-device ML
    implementation(files("libs/tensorflow-lite.aar"))
    implementation(files("libs/tensorflowlite_support_java.aar"))
}