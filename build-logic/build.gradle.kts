plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.tools.sdk.common)
    implementation(libs.squareup.kotlinpoet)
    implementation(libs.snakeyaml.engine)
}

gradlePlugin {
    plugins {
        register("icons") {
            id = "glass.yasan.kepko.icons"
            implementationClass = "glass.yasan.kepko.buildlogic.icons.IconsPlugin"
        }
    }
}
