package glass.yasan.kepko.persistence.internal.legacy

import kotlinx.browser.localStorage

internal class LocalStorageLegacySource : TextLegacySettingsSource() {

    override fun keys(): Set<String> = (0 until localStorage.length).mapNotNullTo(mutableSetOf()) { localStorage.key(it) }

    override fun getText(key: String): String? = localStorage.getItem(key)

    override fun remove(keys: Set<String>) {
        keys.forEach { localStorage.removeItem(it) }
    }
}
