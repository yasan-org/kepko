import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val artifactId = "persistence"

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.roborazzi)
}

kotlin {
    explicitApi()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
        freeCompilerArgs.add("-Xcontext-sensitive-resolution")
        freeCompilerArgs.add("-opt-in=glass.yasan.kepko.foundation.annotation.InternalKepkoApi")
    }

    android {
        namespace = "glass.yasan.kepko.$artifactId"
        compileSdk = libs.versions.android.sdk.compile.get().toInt()
        minSdk = libs.versions.android.sdk.min.get().toInt()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources { enable = true }

        withHostTest {
            isIncludeAndroidResources = true
        }
    }
    jvm()
    iosArm64()
    iosSimulatorArm64()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    js {
        browser()
        binaries.executable()
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        val commonMain = getByName("commonMain") {
            dependencies {
                api(project(":component"))
                implementation(libs.multiplatform.settings.no.arg)
                implementation(libs.jetbrains.compose.ui.tooling.preview)
                implementation(libs.jetbrains.compose.foundation)
                implementation(libs.jetbrains.compose.material3)
                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.jetbrains.compose.ui)
            }
        }
        val commonTest = getByName("commonTest") {
            dependencies {
                implementation(libs.jetbrains.kotlin.test)
                implementation(libs.multiplatform.settings.test)
            }
        }
        val jvmMain = getByName("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest = getByName("jvmTest") {
            dependencies {
                implementation(libs.composable.preview.scanner.jvm)
                implementation(libs.roborazzi.compose.desktop)
                implementation(libs.jetbrains.kotlin.test)
                implementation(libs.jetbrains.compose.ui.test)
            }
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.jetbrains.compose.ui.tooling)
}

compose.resources {
    publicResClass = false
    packageOfResClass = "glass.yasan.kepko.$artifactId"
    generateResClass = auto
}

configure<MavenPublishBaseExtension> {
    coordinates(artifactId = artifactId)
}

tasks.register<Delete>("cleanSnapshots") {
    delete(fileTree("src/jvmTest/snapshots") { include("*.png") })
}

tasks.register("cleanRecordSnapshots") {
    description = "Deletes all snapshots and re-records them"
    dependsOn("cleanSnapshots")
    finalizedBy("recordRoborazziJvm")
}

tasks.register("verifySnapshots") {
    description = "Verifies snapshots match baselines"
    dependsOn("verifyRoborazziJvm")
}

val isCleanRecord = providers.gradleProperty("cleanRecordSnapshots").isPresent ||
    gradle.startParameter.taskNames.any { it.contains("cleanRecordSnapshots") }

tasks.named("jvmTest") {
    mustRunAfter("cleanSnapshots")
    if (isCleanRecord) {
        outputs.upToDateWhen { false }
    }
}
