plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.service) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.pref) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.kover)
}

buildscript {
    dependencies {
        classpath(libs.android.junit5)
    }
}

dependencies {
    kover(project(":domain"))
    kover(project(":repository"))
    kover(project(":viewModel"))
    kover(project(":remoteDatasource"))
    kover(project(":localDatasource"))
}

kover.reports {
    filters.excludes {
        androidGeneratedClasses()
        packages(
            "*.exceptions",
            "*.logger",
            "*.client",
            "*.converter",
            "*.daos",
            "*.paging",
            "*.utils",
            "*.viewmodel.shared",
            "*.dto"
        )
        classes(
            "*State*", "*Effect*", "*AflamiDatabase*", "*Args*"
        )

        // todo: remove this after adding all mapper tests
        classes(
            "*Mapper*"
        )

        // todo: remove this after adding all viewModel and repository tests
        packages(
            "*.viewmodel", "*.repository"
        )

        // todo: remove this after adding related localDatasource tests
        classes(
            "*TvShowLocal*", "*MovieLocal*"
        )

        // todo: remove this after adding related useCase tests
        classes(
            "*GetMovieCastUseCase*", "*GetMovieDetailsUseCase*", "*GetPopularMoviesUseCase*", "*GetUpcomingMoviesUseCase*"
        )
    }
    verify.rule {
        minBound(80)
    }
}
