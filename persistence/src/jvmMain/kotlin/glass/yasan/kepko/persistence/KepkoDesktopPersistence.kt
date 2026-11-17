package glass.yasan.kepko.persistence

import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.persistence.internal.KepkoDataStore
import java.io.File

/**
 * Desktop-only settings for where Kepko stores its preferences.
 */
@ExperimentalKepkoApi
public object KepkoDesktopPersistence {

    private val appIdPattern = Regex("[A-Za-z0-9_-][A-Za-z0-9._-]*")

    /**
     * Gives this app its own preferences file, in `~/.kepko/<appId>/`. Use a stable, unique name such as the
     * app's package name, and set it before the first [PersistentKepkoTheme] is composed; a later change
     * has no effect.
     *
     * When null, the app uses `~/.kepko/` together with every other Kepko desktop app that sets no id. Two
     * running processes that share a file overwrite each other's changes.
     */
    @Volatile
    public var appId: String? = null
        set(value) {
            require(value == null || appIdPattern.matches(value)) {
                "appId may only contain letters, digits, '.', '_' and '-', and can't start with '.': $value"
            }
            field = value
        }

    /**
     * `<home>/.kepko/<appId>/`, or `<home>/.kepko/` shared by every app that sets no [appId].
     */
    internal fun storeFile(home: String): File {
        val root = File(home, ".kepko")
        val directory = appId?.let { File(root, it) } ?: root

        return File(directory, KepkoDataStore.FILE_NAME)
    }
}
