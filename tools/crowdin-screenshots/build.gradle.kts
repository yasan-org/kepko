plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.detekt)
    application
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    testImplementation(libs.jetbrains.kotlin.test)
}

application {
    mainClass = "glass.yasan.crowdin.screenshots.MainKt"
}

// Snapshot directories are passed relative to the repository root.
tasks.named<JavaExec>("run") {
    workingDir = rootDir.resolve("../..")
}

tasks.test {
    useJUnitPlatform()
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom(
        "$rootDir/../../detekt/detekt.yml",
        "$rootDir/../../detekt/detekt-kepko.yml",
    )
}
