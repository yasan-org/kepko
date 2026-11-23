package glass.yasan.kepko.buildlogic.icons

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

public abstract class IconsExtension {
    /** YAML manifest listing the icons and their Material Symbols settings. */
    public abstract val manifest: RegularFileProperty

    /** Directory the `ic_<name>.xml` drawables are written to. */
    public abstract val drawableDirectory: DirectoryProperty

    /** Kotlin source root the generated sources are written to. */
    public abstract val sourceDirectory: DirectoryProperty

    /** Package of the generated sources, which must also hold the Compose `Res` class. */
    public abstract val packageName: Property<String>

    /** Name of the generated object with one `Painter` property per icon. */
    public abstract val iconsName: Property<String>

    /** Name of the generated enum exposing the icons under stable string ids. */
    public abstract val namedIconName: Property<String>
}

/**
 * Adds `syncIcons`, which syncs Material Symbols from a manifest into the module, and
 * `checkIcons`, which fails if the checked-in files differ from what a sync would write.
 * `checkIcons` runs offline unless `--download` is passed.
 */
public class IconsPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<IconsExtension>("kepkoIcons").apply {
            manifest.convention(project.layout.projectDirectory.file("icons.yaml"))
            drawableDirectory.convention(project.layout.projectDirectory.dir("src/commonMain/composeResources/drawable"))
            sourceDirectory.convention(project.layout.projectDirectory.dir("src/commonMain/kotlin"))
            iconsName.convention("Icons")
            namedIconName.convention("NamedIcon")
        }

        fun SyncIconsTask.configureFrom(check: Boolean) {
            group = "kepko"
            manifest.set(extension.manifest)
            drawableDirectory.set(extension.drawableDirectory)
            sourceDirectory.set(extension.sourceDirectory)
            packageName.set(extension.packageName)
            iconsName.set(extension.iconsName)
            namedIconName.set(extension.namedIconName)
            checkOnly.set(check)
            download.convention(!check)
        }

        val syncIcons = project.tasks.register<SyncIconsTask>("syncIcons") {
            description = "Downloads the Material Symbols listed in the icon manifest and regenerates Icons and NamedIcon."
            configureFrom(check = false)
        }
        val checkIcons = project.tasks.register<SyncIconsTask>("checkIcons") {
            description = "Fails if the generated icon sources or the set of drawables are out of sync with the icon manifest. " +
                "Pass --download to also compare the drawable contents."
            configureFrom(check = true)
        }

        // syncIcons rewrites sources and resources, so anything else in the same build waits for it.
        project.tasks.configureEach {
            if (name != syncIcons.name) mustRunAfter(syncIcons)
        }
        project.tasks.named { it == "check" }.configureEach { dependsOn(checkIcons) }
    }
}
