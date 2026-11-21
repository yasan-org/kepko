package glass.yasan.kepko.sample

internal sealed interface Route {

    val path: String

    data object Home : Route {
        override val path = "home"
    }

    data object Theme : Route {
        override val path = "theme"
    }

    data object Profiles : Route {
        override val path = "profiles"
    }

    data class ProfileTheme(val profileId: String) : Route {
        override val path = "$PROFILE_THEME_PREFIX$profileId"
    }

    data object Icons : Route {
        override val path = "icons"
    }

    data object Markdown : Route {
        override val path = "markdown"
    }

    data object Serialization : Route {
        override val path = "serialization"
    }

    data object TitleBar : Route {
        override val path = "title-bar"
    }

    companion object {
        private const val PROFILE_THEME_PREFIX = "profile-theme/"

        fun fromPath(path: String): Route? = when {
            path.startsWith(PROFILE_THEME_PREFIX) -> ProfileTheme(path.removePrefix(PROFILE_THEME_PREFIX))
            else -> listOf(Home, Theme, Profiles, Icons, Markdown, Serialization, TitleBar)
                .firstOrNull { it.path == path }
        }
    }
}
