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
    implementation(platform(libs.androidx.compose.bom))

    // Coil for image loading
    implementation(libs.coil.compose)

    // TensorFlow Lite for on-device ML
    implementation(files("libs/tensorflow-lite.aar"))
    implementation(files("libs/tensorflowlite_support_java.aar"))

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.ml.modeldownloader)

    implementation(libs.androidx.startup.runtime)
}