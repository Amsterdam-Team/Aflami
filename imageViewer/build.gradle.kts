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
    // Android core
    implementation(libs.androidx.core.ktx)

    // Material Design 3
    implementation(libs.androidx.material3)

    // Jetpack Compose BOM and preview tooling
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui.tooling.preview.android)

    // Coil for image loading
    implementation(libs.coil.compose)

    // TensorFlow Lite for on-device ML
    implementation(libs.tensorflow.lite)
    implementation(libs.tensorflow.lite.support)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
