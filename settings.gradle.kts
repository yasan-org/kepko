pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { // Google mirror of Maven Central, avoids rate limits
            allowInsecureContinueWhenDisabled.set(true)
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google()
        maven("https://maven-central.storage-download.googleapis.com/maven2/") { // Google mirror of Maven Central, avoids rate limits
            allowInsecureContinueWhenDisabled.set(true)
        }
        mavenCentral()
    }
}

rootProject.name = "kepko"
include(":resource")
include(":foundation")
include(":component")
include(":serialization")
include(":persistence")
include(":navigation")
include(":sample")
include(":sample:composeApp")
include(":sample:androidApp")
