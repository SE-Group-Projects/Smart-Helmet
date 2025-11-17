pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
    plugins {
        id("com.google.dagger.hilt.android") version "2.48"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
        id("com.android.application") version "8.5.2"
        id("com.google.gms.google-services") version "4.4.2"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://jitpack.io")
    }
}

rootProject.name = "Smart Helmet App"
include(":app")
