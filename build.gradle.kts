import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.plugins.signing.Sign

plugins {
    alias(libs.plugins.jetbrains.kotlin.compose) apply false
    alias(libs.plugins.jetbrains.kotlin.multiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.jetbrains.compose) apply false
    alias(libs.plugins.vanniktech.maven.publish) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.jetbrains.dokka)
    alias(libs.plugins.jetbrains.kover)
}

dependencies {
    dokka(project(":resource"))
    dokka(project(":foundation"))
    dokka(project(":component"))
    dokka(project(":serialization"))
    dokka(project(":persistence"))
    dokka(project(":navigation"))

    kover(project(":resource"))
    kover(project(":foundation"))
    kover(project(":component"))
    kover(project(":serialization"))
    kover(project(":persistence"))
    kover(project(":navigation"))
}

dokka {
    moduleName.set("Kepko")
    dokkaPublications.html {
        includes.from("README.md")
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(
        "$projectDir/detekt/detekt.yml",
        "$projectDir/detekt/detekt-kepko.yml",
    )
    autoCorrect = true
}

allprojects {
    group = "glass.yasan.kepko"
    version = "1.7.2"
}

fun Project.configureDetekt() {
    pluginManager.apply("dev.detekt")
    detekt {
        buildUponDefaultConfig = true
        config.setFrom(
            "$rootDir/detekt/detekt.yml",
            "$rootDir/detekt/detekt-kepko.yml",
        )
        source.from(
            "src/androidMain/kotlin",
            "src/androidTest/kotlin",
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/iosMain/kotlin",
            "src/iosTest/kotlin",
            "src/jvmAndroidMain/kotlin",
            "src/jvmMain/kotlin",
            "src/jvmTest/kotlin"
        )
    }
}

fun Project.configurePublishing() {
    pluginManager.apply("com.vanniktech.maven.publish")
    configure<MavenPublishBaseExtension> {
        publishToMavenCentral(
            automaticRelease = true,
            validateDeployment = DeploymentValidation.NONE,
        )

        signAllPublications()

        pom {
            name.set("Kepko")
            description.set("An opinionated design system for Compose Multiplatform.")
            inceptionYear.set("2025")
            url.set("https://github.com/yasanglass/kepko/")
            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    distribution.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }
            developers {
                developer {
                    id.set("yasanglass")
                    name.set("Yasan Glass")
                    url.set("https://github.com/yasanglass/")
                }
            }
            scm {
                url.set("https://github.com/yasanglass/kepko/")
                connection.set("scm:git:git://github.com/yasanglass/kepko.git")
                developerConnection.set("scm:git:ssh://git@github.com/yasanglass/kepko.git")
            }
        }
    }

    tasks.withType<Sign>().configureEach {
        isEnabled = gradle.startParameter.taskNames.none { it.contains("MavenLocal") }
    }
}

subprojects {
    val isSample = path.contains("sample")

    configureDetekt()

    if (isSample.not()) {
        pluginManager.apply("org.jetbrains.dokka")
        pluginManager.apply("org.jetbrains.kotlinx.kover")
        configurePublishing()

        tasks.withType<Jar>().configureEach {
            from(rootProject.file("LICENSE")) {
                into("META-INF")
            }
            from(rootProject.file("NOTICE")) {
                into("META-INF")
            }
        }
    }
}
