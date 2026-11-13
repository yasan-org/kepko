package glass.yasan.kepko.persistence

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.dimension.Dimensions
import glass.yasan.kepko.foundation.shape.ShapeTokens
import glass.yasan.kepko.foundation.shape.Shapes
import glass.yasan.kepko.foundation.theme.ColorPalette
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.foundation.theme.isSystemInDarkTheme
import glass.yasan.kepko.persistence.internal.PreviewPersistenceManager
import glass.yasan.kepko.persistence.internal.SingletonPersistenceManager

/**
 * A [KepkoTheme] wrapper that automatically persists and restores theme preferences.
 *
 * - Use [LocalKepkoColorPalette] to access the active color palette in [content].
 * - Use [LocalKepkoPersistenceManager] to access the [PersistenceManager] instance in [content].
 * - Use [PersistentPreferenceThemeScreen] to allow users to configure theme preferences.
 */
@ExperimentalKepkoApi
@Composable
public fun PersistentKepkoTheme(
    dimensions: Dimensions = KepkoTheme.dimensions,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    profileId: String? = null,
    content: @Composable () -> Unit,
) {
    val persistenceManager = rememberLoadedPersistenceManager(
        loadOrNull = SingletonPersistenceManager::instanceOrBlockingLoad,
        await = SingletonPersistenceManager::awaitInstance,
    ) ?: return

    PersistentKepkoTheme(
        persistenceManager = persistenceManager,
        dimensions = dimensions,
        isSystemInDarkTheme = isSystemInDarkTheme,
        profileId = profileId,
        content = content,
    )
}

/**
 * A [KepkoTheme] wrapper that automatically persists and restores theme preferences.
 *
 * - Use [LocalKepkoColorPalette] to access the active color palette in [content].
 * - Use [LocalKepkoPersistenceManager] to access the [PersistenceManager] instance in [content].
 * - Use [PersistentPreferenceThemeScreen] to allow users to configure theme preferences.
 */
@ExperimentalKepkoApi
@Composable
public fun PersistentKepkoTheme(
    persistenceManager: PersistenceManager,
    dimensions: Dimensions = KepkoTheme.dimensions,
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    profileId: String? = null,
    content: @Composable () -> Unit,
) {
    val palette = persistenceManager.getActivePalette(
        profileId = profileId,
        isSystemInDarkTheme = isSystemInDarkTheme,
    )

    val resolvedDimensions = dimensions.copy(
        borderThickness = persistenceManager.outline,
    )

    val roundness = persistenceManager.roundness
    val resolvedShapes = Shapes(
        extraSmall = RoundedCornerShape(ShapeTokens.extraSmallCornerRadius * roundness),
        small = RoundedCornerShape(ShapeTokens.smallCornerRadius * roundness),
        medium = RoundedCornerShape(ShapeTokens.mediumCornerRadius * roundness),
        large = RoundedCornerShape(ShapeTokens.largeCornerRadius * roundness),
        extraLarge = RoundedCornerShape(ShapeTokens.extraLargeCornerRadius * roundness),
    )

    KepkoTheme(
        palette = palette,
        grayscale = persistenceManager.isGrayscaleEnabled(profileId),
        dimensions = resolvedDimensions,
        shapes = resolvedShapes,
    ) {
        CompositionLocalProvider(
            LocalKepkoPersistenceManager provides persistenceManager,
            LocalKepkoColorPalette provides palette,
            content = content,
        )
    }
}

/**
 * Returns [loadOrNull]'s manager, or null until [await] delivers it.
 *
 * Only web gets null: it can't block, so nothing is composed until the preferences load.
 */
@Composable
internal fun rememberLoadedPersistenceManager(
    loadOrNull: () -> PersistenceManager?,
    await: suspend () -> PersistenceManager,
): PersistenceManager? {
    val loadedManager = remember { loadOrNull() }

    return produceState(initialValue = loadedManager) {
        if (value == null) value = await()
    }.value
}

/**
 * A [PersistentKepkoTheme] implementation for preview purposes.
 *
 * The active [ColorPalette] will always be based on the [isSystemInDarkTheme] parameter.
 *
 * @see PreviewPersistenceManager
 */
@OptIn(ExperimentalKepkoApi::class)
@Composable
public fun PreviewPersistentKepkoTheme(
    isSystemInDarkTheme: Boolean = false,
    configure: PreviewPersistenceManager.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    PersistentKepkoTheme(
        persistenceManager = remember { PreviewPersistenceManager().apply(configure) },
        isSystemInDarkTheme = isSystemInDarkTheme,
        content = content,
    )
}
