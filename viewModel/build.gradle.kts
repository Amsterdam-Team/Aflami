import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())
val movieSignUp: String = properties.getProperty("movieSignUp") ?: ""
val movieResetPassword: String = properties.getProperty("movieResetPassword") ?: ""

plugins {
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.android.junit5)
    alias(libs.plugins.kover)
    alias(libs.plugins.ksp)
}

android {
    namespace = libs.versions.namespaceViewModel.get()

    buildFeatures { buildConfig = true }

    defaultConfig {
        buildConfigField("String", "MOVIE_SIGN_UP_URL", movieSignUp)
        buildConfigField("String", "MOVIE_RESET_PASSWORD_URL", movieResetPassword)
    }
}

dependencies {
    modulesDependencies()
    coroutinesDependencies()
    kotlinExtensionsDependencies()
    appCompactDependencies()
    lifeCycleDependencies()
    dateTimeDependencies()
    pagingDependencies()
    testDependencies()
    hiltDependencies()
}

private fun DependencyHandlerScope.modulesDependencies() {
    api(projects.domain)
}

private fun DependencyHandlerScope.coroutinesDependencies() {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

private fun DependencyHandlerScope.kotlinExtensionsDependencies() {
    implementation(libs.androidx.core.ktx)
}

private fun DependencyHandlerScope.appCompactDependencies() {
    implementation(libs.androidx.appcompat)
}

private fun DependencyHandlerScope.lifeCycleDependencies() {
    implementation(libs.androidx.lifecycle.runtime.ktx)
}

private fun DependencyHandlerScope.dateTimeDependencies() {
    implementation(libs.kotlinx.datetime)
}

private fun DependencyHandlerScope.pagingDependencies() {
    implementation(libs.androidx.paging.runtime)
}

private fun DependencyHandlerScope.testDependencies() {
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.truth)
    testImplementation(kotlin("test"))
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation(libs.androidx.paging.testing)
}

private fun DependencyHandlerScope.hiltDependencies() {
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}


kover.reports {
    filters.excludes {
        androidGeneratedClasses()
        packages(
            "*.paging",
            "*.utils",
            "*.viewmodel.shared",
        )
        classes(
            "*State*", "*Effect*", "*Args*", "*Hilt*", "*_Factory*"
        )

        // todo: remove the equivalent tests after increasing or fixing them
        packages(
            "*.application",
            "*.continueWatching",
            "*.letsPlay",
            "*.onboarding",
            "*.profile",
            "*.keywordSearch",
            "*.mapper",
            "*.seriesDetails",
            "*.watchHistory",
            "*.topRated"
        )
    }

    verify.rule {
        minBound(80)
    }
}
