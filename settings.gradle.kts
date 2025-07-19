pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        flatDir {
            dirs ("libs")
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
        flatDir {
            dirs("libs")
        }
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
include(":blurred")
