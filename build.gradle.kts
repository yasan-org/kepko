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

    // GitHub Packages rejects re-uploading a file, so re-runs skip publications that are fully there
    // and stop with a clear error on one an earlier run only partly uploaded.
    val gitHubPackagesUsername = providers.gradleProperty("GitHubPackagesUsername")
    val gitHubPackagesPassword = providers.gradleProperty("GitHubPackagesPassword")
    val signingKey = providers.gradleProperty("signingInMemoryKey")
    tasks.withType<PublishToMavenRepository>().configureEach {
        if (name.endsWith("ToGitHubPackagesRepository")) {
            doFirst("Check GitHub Packages") {
                val task = this as PublishToMavenRepository
                val publication = task.publication
                val coordinates = "${publication.groupId}:${publication.artifactId}:${publication.version}"
                val base = "${task.repository.url.toString().trimEnd('/')}/" +
                    "${publication.groupId.replace('.', '/')}/${publication.artifactId}/${publication.version}/" +
                    "${publication.artifactId}-${publication.version}"
                // Signatures are added below, whether or not the publication lists them as artifacts.
                val files = publication.artifacts.filterNot { it.extension.endsWith("asc") }.map { artifact ->
                    base + artifact.classifier?.let { "-$it" }.orEmpty() + ".${artifact.extension}"
                } + "$base.pom" + "$base.module"
                val expected = if (signingKey.isPresent) files + files.map { "$it.asc" } else files
                val auth = "${gitHubPackagesUsername.orNull}:${gitHubPackagesPassword.orNull}"

                // Returns the file's content (empty unless readBody), or null when it isn't there.
                fun fetch(url: String, readBody: Boolean = false): String? {
                    val connection = URI(url).toURL().openConnection() as HttpURLConnection
                    connection.connectTimeout = 10_000
                    connection.readTimeout = 30_000
                    connection.setRequestProperty(
                        "Authorization",
                        "Basic ${Base64.getEncoder().encodeToString(auth.toByteArray())}",
                    )
                    try {
                        return when (val code = connection.responseCode) {
                            200 -> if (readBody) connection.inputStream.bufferedReader().use { it.readText() } else ""
                            404 -> null
                            else -> throw GradleException("Could not check $url on GitHub Packages: HTTP $code")
                        }
                    } finally {
                        connection.disconnect()
                    }
                }

                // Gradle uploads maven-metadata.xml last, so it lists the version only once every file is up.
                val metadata = "${base.substringBeforeLast('/').substringBeforeLast('/')}/maven-metadata.xml"
                val listed = fetch(metadata, readBody = true)?.contains("<version>${publication.version}</version>")
                val missing = expected.filter { fetch(it) == null } +
                    listOfNotNull(metadata.takeUnless { listed == true })
                when {
                    missing.isEmpty() -> {
                        task.logger.lifecycle("$coordinates is already on GitHub Packages")
                        throw StopExecutionException()
                    }
                    missing.size == expected.size + 1 -> Unit
                    else -> throw GradleException(
                        "$coordinates is only partly on GitHub Packages, missing " +
                            missing.joinToString { it.substringAfterLast('/') } +
                            ". Delete that package version on GitHub, then publish again.",
                    )
                }
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
