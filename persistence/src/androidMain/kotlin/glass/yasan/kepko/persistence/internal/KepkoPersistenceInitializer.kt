package glass.yasan.kepko.persistence.internal

import android.content.Context
import androidx.startup.Initializer
import glass.yasan.kepko.foundation.annotation.InternalKepkoApi

internal var applicationContext: Context? = null
    private set

/**
 * Captures the application [Context] for Kepko's DataStore and starts loading it.
 *
 * Registered in this library's manifest.
 */
@InternalKepkoApi
public class KepkoPersistenceInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        applicationContext = context.applicationContext
        SingletonPersistenceManager.preload()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
