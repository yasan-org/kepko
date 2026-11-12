package glass.yasan.kepko.persistence

import android.content.SharedPreferences
import glass.yasan.kepko.persistence.internal.SharedPreferencesLegacySource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

internal class SharedPreferencesLegacySourceTest {

    private companion object {
        const val STYLE_KEY = "glass.yasan.kepko.style.primary"
        const val GRAYSCALE_KEY = "glass.yasan.kepko.grayscale"
        const val ROUNDNESS_KEY = "glass.yasan.kepko.dimension.roundness"
        const val APP_KEY = "app.theme"
    }

    @Test
    fun givenValuesStoredTheWayMultiplatformSettingsDid_whenRead_thenReturnsEachWithItsType() {
        // Given
        val source = SharedPreferencesLegacySource(
            FakeSharedPreferences(STYLE_KEY to "black", GRAYSCALE_KEY to true, ROUNDNESS_KEY to 0.5f),
        )

        // Then
        assertEquals(setOf(STYLE_KEY, GRAYSCALE_KEY, ROUNDNESS_KEY), source.keys())
        assertEquals("black", source.getString(STYLE_KEY))
        assertEquals(true, source.getBoolean(GRAYSCALE_KEY))
        assertEquals(0.5f, source.getFloat(ROUNDNESS_KEY))
    }

    @Test
    fun givenAValueOfAnotherType_whenRead_thenReturnsNull() {
        // Given
        val source = SharedPreferencesLegacySource(FakeSharedPreferences(STYLE_KEY to 3, GRAYSCALE_KEY to "true"))

        // Then
        assertNull(source.getString(STYLE_KEY))
        assertNull(source.getBoolean(GRAYSCALE_KEY))
        assertNull(source.getFloat(ROUNDNESS_KEY))
    }

    @Test
    fun givenKepkoAndAppKeys_whenKepkoKeysRemoved_thenKeepsAppKeys() {
        // Given
        val preferences = FakeSharedPreferences(STYLE_KEY to "black", APP_KEY to "dark")
        val source = SharedPreferencesLegacySource(preferences)

        // When
        source.remove(setOf(STYLE_KEY))

        // Then
        assertEquals(mapOf<String, Any?>(APP_KEY to "dark"), preferences.all)
    }
}

private class FakeSharedPreferences(vararg values: Pair<String, Any>) : SharedPreferences {

    private val values = mutableMapOf<String, Any?>(*values)

    override fun getAll(): MutableMap<String, *> = values.toMutableMap()

    override fun getString(key: String, defValue: String?): String? = values[key] as? String ?: defValue

    @Suppress("UNCHECKED_CAST")
    override fun getStringSet(key: String, defValues: MutableSet<String>?): MutableSet<String>? =
        values[key] as? MutableSet<String> ?: defValues

    override fun getInt(key: String, defValue: Int): Int = values[key] as? Int ?: defValue

    override fun getLong(key: String, defValue: Long): Long = values[key] as? Long ?: defValue

    override fun getFloat(key: String, defValue: Float): Float = values[key] as? Float ?: defValue

    override fun getBoolean(key: String, defValue: Boolean): Boolean = values[key] as? Boolean ?: defValue

    override fun contains(key: String): Boolean = key in values

    override fun edit(): SharedPreferences.Editor = Editor()

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
    ) = Unit

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener,
    ) = Unit

    private inner class Editor : SharedPreferences.Editor {

        private val removed = mutableSetOf<String>()
        private val added = mutableMapOf<String, Any?>()

        override fun putString(key: String, value: String?) = put(key, value)

        override fun putStringSet(key: String, values: MutableSet<String>?) = put(key, values)

        override fun putInt(key: String, value: Int) = put(key, value)

        override fun putLong(key: String, value: Long) = put(key, value)

        override fun putFloat(key: String, value: Float) = put(key, value)

        override fun putBoolean(key: String, value: Boolean) = put(key, value)

        override fun remove(key: String): SharedPreferences.Editor = apply { removed += key }

        override fun clear(): SharedPreferences.Editor = apply { removed += values.keys }

        override fun commit(): Boolean {
            apply()
            return true
        }

        override fun apply() {
            values -= removed
            values += added
        }

        private fun put(key: String, value: Any?): SharedPreferences.Editor = apply { added[key] = value }
    }
}
