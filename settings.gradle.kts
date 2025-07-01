pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Aflami"
include(":app")
include(":ui")
include(":designSystem")
include(":viewModel")
include(":remoteDatasource")
include(":localDatasource")
include(":domain")
include(":entity")
include(":repository")
include(":imageViewer")
