package glass.yasan.kepko.persistence

import androidx.datastore.preferences.core.emptyPreferences
import glass.yasan.kepko.persistence.internal.PreferencesStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
internal class PreferencesStoreTest {

    private companion object {
        const val STRING_KEY = "glass.yasan.kepko.test.string"
        const val BOOLEAN_KEY = "glass.yasan.kepko.test.boolean"
        const val FLOAT_KEY = "glass.yasan.kepko.test.float"
    }

    private fun TestScope.createStore(dataStore: InMemoryPreferencesDataStore): PreferencesStore =
        PreferencesStore(dataStore = dataStore, initial = dataStore.current, scope = backgroundScope)

    @Test
    fun givenValuesPut_whenIdle_thenDataStoreHoldsThem() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore()
        val store = createStore(dataStore)

        // When
        store.putString(STRING_KEY, "value")
        store.putBoolean(BOOLEAN_KEY, true)
        store.putFloat(FLOAT_KEY, 1.5f)
        runCurrent()

        // Then
        assertEquals(
            testPreferences(STRING_KEY to "value", BOOLEAN_KEY to true, FLOAT_KEY to 1.5f),
            dataStore.current,
        )
    }

    @Test
    fun givenValuesPut_whenReadBack_thenReturnsThemBeforeTheyAreWritten() = runTest {
        // Given
        val store = createStore(InMemoryPreferencesDataStore())

        // When
        store.putString(STRING_KEY, "value")
        store.putBoolean(BOOLEAN_KEY, false)
        store.putFloat(FLOAT_KEY, 2f)

        // Then
        assertEquals("value", store.getStringOrNull(STRING_KEY))
        assertEquals(false, store.getBooleanOrNull(BOOLEAN_KEY))
        assertEquals(2f, store.getFloatOrNull(FLOAT_KEY))
    }

    @Test
    fun givenRapidPuts_whenIdle_thenWritesOnceWithTheLatestValue() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore()
        val store = createStore(dataStore)

        // When
        store.putFloat(FLOAT_KEY, 1f)
        store.putFloat(FLOAT_KEY, 2f)
        store.putFloat(FLOAT_KEY, 3f)
        runCurrent()

        // Then
        assertEquals(1, dataStore.writes)
        assertEquals(testPreferences(FLOAT_KEY to 3f), dataStore.current)
    }

    @Test
    fun givenAFailedWrite_whenTheNextValueIsPut_thenBothAreWritten() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore()
        val store = createStore(dataStore)
        dataStore.failWrites = true
        store.putString(STRING_KEY, "first")
        runCurrent()

        // When
        dataStore.failWrites = false
        store.putBoolean(BOOLEAN_KEY, true)
        runCurrent()

        // Then
        assertEquals(testPreferences(STRING_KEY to "first", BOOLEAN_KEY to true), dataStore.current)
    }

    @Test
    fun givenAFailedWrite_whenNothingElseChanges_thenItIsRetriedLater() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore()
        val store = createStore(dataStore)
        dataStore.failWrites = true
        store.putString(STRING_KEY, "value")
        runCurrent()

        // When
        dataStore.failWrites = false
        advanceTimeBy(2.seconds)

        // Then
        assertEquals(testPreferences(STRING_KEY to "value"), dataStore.current)
    }

    @Test
    fun givenAWrittenKey_whenAnotherWriterChangesIt_thenALaterPutDoesNotOverwriteIt() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore()
        val store = createStore(dataStore)
        store.putString(STRING_KEY, "mine")
        runCurrent()
        dataStore.updateData { testPreferences(STRING_KEY to "theirs") }

        // When
        store.putFloat(FLOAT_KEY, 1f)
        runCurrent()

        // Then
        assertEquals(testPreferences(STRING_KEY to "theirs", FLOAT_KEY to 1f), dataStore.current)
    }

    @Test
    fun givenAKeyStoredAsAnotherType_whenPut_thenReplacesIt() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore(testPreferences(STRING_KEY to "text"))
        val store = createStore(dataStore)

        // When
        store.putBoolean(STRING_KEY, true)
        runCurrent()

        // Then
        assertEquals(testPreferences(STRING_KEY to true), dataStore.current)
    }

    @Test
    fun givenPendingChanges_whenFlushed_thenReturnsOnceTheyAreWritten() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore()
        val store = createStore(dataStore)
        store.putString(STRING_KEY, "value")

        // When
        val flushed = store.flush(timeout = 1.seconds)

        // Then
        assertTrue(flushed)
        assertEquals(testPreferences(STRING_KEY to "value"), dataStore.current)
    }

    @Test
    fun givenWritesThatKeepFailing_whenFlushed_thenGivesUpAfterTheTimeout() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore().apply { failWrites = true }
        val store = createStore(dataStore)
        store.putString(STRING_KEY, "value")

        // Then
        assertEquals(false, store.flush(timeout = 1.seconds))
    }

    @Test
    fun givenAStoredValue_whenRemoved_thenDataStoreNoLongerHoldsIt() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore(testPreferences(FLOAT_KEY to 1f, STRING_KEY to "kept"))
        val store = createStore(dataStore)

        // When
        store.remove(FLOAT_KEY)
        runCurrent()

        // Then
        assertNull(store.getFloatOrNull(FLOAT_KEY))
        assertEquals(testPreferences(STRING_KEY to "kept"), dataStore.current)
    }

    @Test
    fun givenAStoredValue_whenReadAsAnotherType_thenReturnsNull() {
        // Given
        val store = testPreferencesStore(BOOLEAN_KEY to true)

        // Then
        assertEquals(true, store.getBooleanOrNull(BOOLEAN_KEY))
        assertNull(store.getStringOrNull(BOOLEAN_KEY))
        assertNull(store.getFloatOrNull(BOOLEAN_KEY))
    }

    @Test
    fun givenStoredValues_whenLoaded_thenReadsThemBack() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore(testPreferences(STRING_KEY to "value", FLOAT_KEY to 4f))

        // When
        val store = PreferencesStore.load(dataStore = dataStore, scope = backgroundScope)

        // Then
        assertEquals("value", store.getStringOrNull(STRING_KEY))
        assertEquals(4f, store.getFloatOrNull(FLOAT_KEY))
    }

    @Test
    fun givenAnUnreadableDataStore_whenLoaded_thenStartsEmptyAndStillWrites() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore().apply { failReads = true }

        // When
        val store = PreferencesStore.load(dataStore = dataStore, scope = backgroundScope)
        store.putString(STRING_KEY, "value")
        runCurrent()

        // Then
        assertNull(store.getBooleanOrNull(BOOLEAN_KEY))
        assertEquals(testPreferences(STRING_KEY to "value"), dataStore.current)
    }

    @Test
    fun givenAStoreThatFailedToLoad_whenAValueIsPut_thenValuesItNeverReadSurvive() = runTest {
        // Given
        val dataStore = InMemoryPreferencesDataStore(testPreferences(STRING_KEY to "kept")).apply { failReads = true }
        val store = PreferencesStore.load(dataStore = dataStore, scope = backgroundScope)
        dataStore.failReads = false

        // When
        store.putFloat(FLOAT_KEY, 1f)
        runCurrent()

        // Then
        assertEquals(testPreferences(STRING_KEY to "kept", FLOAT_KEY to 1f), dataStore.current)
    }

    @Test
    fun givenAStoreScopeThatIsGone_whenValuesPut_thenTheyAreStillReadable() {
        // Given
        val store = PreferencesStore(
            dataStore = InMemoryPreferencesDataStore(),
            initial = emptyPreferences(),
            scope = CoroutineScope(Job().apply { cancel() }),
        )

        // When
        store.putString(STRING_KEY, "value")

        // Then
        assertEquals("value", store.getStringOrNull(STRING_KEY))
    }
}
