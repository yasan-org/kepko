import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.roborazzi)
}

kotlin {
    applyDefaultHierarchyTemplate()

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-sensitive-resolution")
        freeCompilerArgs.add("-opt-in=glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi")
        freeCompilerArgs.add("-opt-in=glass.yasan.kepko.foundation.annotation.InternalKepkoApi")
    }

    android {
        namespace = "glass.yasan.kepko.sample.shared"
        compileSdk = libs.versions.sample.android.sdk.compile.get().toInt()
        minSdk = libs.versions.sample.android.sdk.min.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        androidResources { enable = true }
    }

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    js {
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
            }
        }
        binaries.executable()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":persistence"))
                implementation(project(":serialization"))

                implementation(libs.jetbrains.compose.runtime)
                implementation(libs.jetbrains.compose.foundation)
                implementation(libs.jetbrains.compose.material3)
                implementation(libs.jetbrains.compose.ui)
                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.jetbrains.compose.ui.tooling.preview)
                implementation(libs.androidx.navigation.compose)
                implementation(libs.platformtools.darkmodedetector)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val nonMobileMain = create("nonMobileMain") {
            dependsOn(commonMain.get())
        }
        jvmMain {
            dependsOn(nonMobileMain)
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        jsMain { dependsOn(nonMobileMain) }
        wasmJsMain { dependsOn(nonMobileMain) }
        jvmTest {
            dependencies {
                implementation(project(":resource"))
                implementation(libs.composable.preview.scanner.jvm)
                implementation(libs.roborazzi.compose.desktop)
                implementation(libs.jetbrains.kotlin.test)
                implementation(libs.jetbrains.compose.ui.test)
            }
        }
        androidMain {
        }
    }
}

dependencies {
    "androidRuntimeClasspath"(libs.jetbrains.compose.ui.tooling)
}

tasks.register<Delete>("cleanSnapshots") {
    delete(fileTree("assets/readme") { include("*.png") })
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

compose.desktop {
    application {
        mainClass = "glass.yasan.kepko.sample.MainKt"

        nativeDistributions {
            targetFormats(
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Deb,
                TargetFormat.Rpm,
                TargetFormat.Exe,
                TargetFormat.Pkg,
            )
            packageName = "Kepko"
            packageVersion = project.version.toString()
            description = "Explore the Kepko design system for Compose Multiplatform"
            licenseFile.set(rootProject.file("LICENSE"))
            macOS {
                iconFile.set(project.file("src/jvmMain/resources/app_icon.icns"))
                bundleID = "glass.yasan.kepko.sample"
                dockName = "Kepko"
            }
            windows {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/app_icon.png"))
            }
            linux {
                iconFile.set(project.file("src/commonMain/composeResources/drawable/app_icon.png"))
                debMaintainer = "yasanglass@gmail.com"
                appCategory = "Development"
                rpmLicenseType = "Apache-2.0"
                menuGroup = "Development"
            }
        }
    }
}
