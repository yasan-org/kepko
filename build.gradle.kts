import com.vanniktech.maven.publish.DeploymentValidation
import com.vanniktech.maven.publish.MavenPublishBaseExtension
import org.gradle.plugins.signing.Sign
import java.net.HttpURLConnection
import java.net.URI
import java.util.Base64

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
    version = "1.8.0"
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

    configure<PublishingExtension> {
        repositories.maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/yasan-org/kepko")
            credentials(PasswordCredentials::class)
        }
    }

    // GitHub Packages rejects re-uploading a version, so re-runs skip publications already there.
    val gitHubPackagesUsername = providers.gradleProperty("GitHubPackagesUsername")
    val gitHubPackagesPassword = providers.gradleProperty("GitHubPackagesPassword")
    tasks.withType<PublishToMavenRepository>().configureEach {
        if (name.endsWith("ToGitHubPackagesRepository")) {
            onlyIf("publication is not on GitHub Packages yet") { task ->
                task as PublishToMavenRepository
                val publication = task.publication
                val pomUrl = "${task.repository.url.toString().trimEnd('/')}/" +
                    "${publication.groupId.replace('.', '/')}/${publication.artifactId}/${publication.version}/" +
                    "${publication.artifactId}-${publication.version}.pom"
                val auth = "${gitHubPackagesUsername.orNull}:${gitHubPackagesPassword.orNull}"
                val connection = URI(pomUrl).toURL().openConnection() as HttpURLConnection
                connection.setRequestProperty(
                    "Authorization",
                    "Basic ${Base64.getEncoder().encodeToString(auth.toByteArray())}",
                )
                val published = runCatching { connection.responseCode == 200 }.getOrDefault(false)
                connection.disconnect()
                if (published) {
                    task.logger.lifecycle("${publication.artifactId}:${publication.version} is already on GitHub Packages")
                }
                !published
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
