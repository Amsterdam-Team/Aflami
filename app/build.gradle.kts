plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.service)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.pref)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = libs.versions.namespaceAflami.get()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = libs.versions.namespaceAflami.get()
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("arm64-v8a", "armabi-v7a"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            manifestPlaceholders["crashlyticsCollectionEnabled"] = true
        }
        debug {
            manifestPlaceholders["crashlyticsCollectionEnabled"] = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    appModulesDependencies()
    firebaseDependencies()
    datastoreDependencies()
    jsonSerializationDependencies()
    roomDependencies()
    androidxRuntimeDependencies()
    hiltDependencies()
}

private fun DependencyHandlerScope.appModulesDependencies() {
    implementation(projects.ui)
    implementation(projects.viewModel)
    implementation(projects.localDatasource)
    implementation(projects.remoteDatasource)
    implementation(projects.domain)
    implementation(projects.repository)
}

private fun DependencyHandlerScope.firebaseDependencies() {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.perf)
}

private fun DependencyHandlerScope.datastoreDependencies() {
    implementation(libs.androidx.datastore.preferences)
}

private fun DependencyHandlerScope.jsonSerializationDependencies() {
    implementation(libs.kotlinx.serialization.json)
}

private fun DependencyHandlerScope.roomDependencies() {
    implementation(libs.androidx.room.runtime)
}

fun DependencyHandlerScope.androidxRuntimeDependencies() {
    implementation(libs.androidx.runtime)
}

fun DependencyHandlerScope.hiltDependencies() {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}