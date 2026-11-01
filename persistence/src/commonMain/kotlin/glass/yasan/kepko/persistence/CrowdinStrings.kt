package glass.yasan.kepko.persistence

/**
 * Links a [PreviewWithTest] preview to the string [keys] it shows, so its snapshot is uploaded to
 * Crowdin as a screenshot for those strings. Each string is highlighted wherever its text appears
 * in the snapshot.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
internal annotation class CrowdinStrings(vararg val keys: String)
