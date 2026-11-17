package glass.yasan.kepko.persistence.internal

import android.content.Context
import androidx.startup.Initializer
import glass.yasan.kepko.foundation.annotation.InternalKepkoApi

/**
 * Captures the application [Context] for Kepko's DataStore and starts loading it.
 *
 * Registered in this library's manifest.
 */
@InternalKepkoApi
public class KepkoPersistenceInitializer : Initializer<Unit> {

    internal companion object {

        private var applicationContext: Context? = null

        fun requireContext(): Context = checkNotNull(applicationContext) {
            "Kepko persistence isn't initialized. " +
                "Keep androidx.startup's InitializationProvider in the merged manifest."
        }
    }

    override fun create(context: Context) {
        applicationContext = context.applicationContext
        SingletonPersistenceManager.preload()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}
