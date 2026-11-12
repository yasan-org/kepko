package glass.yasan.kepko.persistence.internal

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.persistence.PersistenceManager
import glass.yasan.kepko.persistence.PersistenceManager.Companion.PALETTE_ID_SYSTEM
import glass.yasan.kepko.persistence.ProfilePersistenceManager

internal class PersistenceManagerImpl(
    private val store: PreferencesStore,
) : PersistenceManager {

    internal companion object {
        private const val PREFIX = "glass.yasan.kepko"

        @VisibleForTesting
        const val KEY_STYLE = "$PREFIX.style.primary"

        @VisibleForTesting
        const val KEY_LIGHT_STYLE = "$PREFIX.style.light"

        @VisibleForTesting
        const val KEY_DARK_STYLE = "$PREFIX.style.dark"

        @VisibleForTesting
        const val KEY_GRAYSCALE = "$PREFIX.grayscale"

        @VisibleForTesting
        const val KEY_OUTLINE = "$PREFIX.dimension.outline"

        @VisibleForTesting
        const val KEY_ROUNDNESS = "$PREFIX.dimension.roundness"

        @VisibleForTesting
        const val KEY_LIST_PANE_WIDTH = "$PREFIX.dimension.list_pane_width"
    }

    override val profileManager: ProfilePersistenceManager = ProfilePersistenceManagerImpl(store)

    internal fun resolvedPalette(
        isSystemInDarkTheme: Boolean,
    ): ColorPalette = getPalettePrimary(null) ?: if (isSystemInDarkTheme) paletteDark else paletteLight

    private var _palettePrimary by mutableStateOf(
        store.getStringOrNull(KEY_STYLE)?.let { ColorPalette.fromIdOrNull(it) }
    )

    override fun getPalettePrimary(profileId: String?): ColorPalette? {
        if (profileId != null) {
            val profilePalette = profileManager.getProfilePalette(profileId)
            if (profilePalette != null) return profilePalette
        }

        return _palettePrimary
    }

    override fun setPalettePrimary(profileId: String?, value: ColorPalette?) {
        if (profileId != null) {
            profileManager.setProfilePalette(profileId, value)
        } else {
            _palettePrimary = value
            store.putString(KEY_STYLE, value?.id ?: PALETTE_ID_SYSTEM)
        }
    }

    private var _paletteLight by mutableStateOf(
        value = ColorPalette.fromIdOrNull(
            id = store.getStringOrNull(KEY_LIGHT_STYLE)
        ) ?: getDefaultSnapshot().paletteLight
    )
    override var paletteLight: ColorPalette
        get() = _paletteLight
        set(value) {
            _paletteLight = value
            store.putString(KEY_LIGHT_STYLE, value.id)
        }

    private var _paletteDark by mutableStateOf(
        value = ColorPalette.fromIdOrNull(
            id = store.getStringOrNull(KEY_DARK_STYLE)
        ) ?: getDefaultSnapshot().paletteDark
    )
    override var paletteDark: ColorPalette
        get() = _paletteDark
        set(value) {
            _paletteDark = value
            store.putString(KEY_DARK_STYLE, value.id)
        }

    private var _grayscale by mutableStateOf(
        store.getBooleanOrNull(KEY_GRAYSCALE) ?: getDefaultSnapshot().grayscale
    )

    override fun isGrayscaleEnabled(profileId: String?): Boolean {
        if (profileId != null) {
            val profileGrayscale = profileManager.getProfileGrayscale(profileId)
            if (profileGrayscale != null) return profileGrayscale
        }

        return _grayscale
    }

    override fun setGrayscaleEnabled(profileId: String?, value: Boolean) {
        if (profileId != null) {
            profileManager.setProfileGrayscale(profileId, value)
        } else {
            _grayscale = value
            store.putBoolean(KEY_GRAYSCALE, value)
        }
    }

    private var _outline by mutableStateOf(
        (store.getFloatOrNull(KEY_OUTLINE) ?: getDefaultSnapshot().outline.value).dp
    )
    override var outline: Dp
        get() = _outline
        set(value) {
            _outline = value
            store.putFloat(KEY_OUTLINE, value.value)
        }

    private var _roundness by mutableStateOf(
        store.getFloatOrNull(KEY_ROUNDNESS) ?: getDefaultSnapshot().roundness
    )
    override var roundness: Float
        get() = _roundness
        set(value) {
            _roundness = value
            store.putFloat(KEY_ROUNDNESS, value)
        }

    private var _listPaneWidth by mutableStateOf(store.getFloatOrNull(KEY_LIST_PANE_WIDTH)?.dp)

    @ExperimentalKepkoApi
    override var listPaneWidth: Dp?
        get() = _listPaneWidth
        set(value) {
            _listPaneWidth = value
            if (value == null) {
                store.remove(KEY_LIST_PANE_WIDTH)
            } else {
                store.putFloat(KEY_LIST_PANE_WIDTH, value.value)
            }
        }
}
