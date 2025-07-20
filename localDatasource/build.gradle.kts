plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.aflami.custom.plugin)
    alias(libs.plugins.kover)
}

android {
    namespace = "com.example.localdatasource"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
}

dependencies {
    implementation(project(":repository"))

    // Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.androidx.room.testing)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Date and Time
    implementation(libs.kotlinx.datetime)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit.jupiter)
    testImplementation(kotlin("test"))
}

kover.reports {
    filters.excludes {
        androidGeneratedClasses()
        classes(
            "*.MovieLocalDataSourceImpl",
            "*.RecentSearchLocalDataSourceImpl",
            "*.TvShowLocalDataSourceImpl"
        )
    }
    filters.includes {
        packages("*.datasource")
    }

    verify {
        rule {
            minBound(80)
        }
    }
}
