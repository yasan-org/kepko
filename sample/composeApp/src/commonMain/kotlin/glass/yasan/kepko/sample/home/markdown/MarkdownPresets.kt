package glass.yasan.kepko.sample.home.markdown

internal object MarkdownPresets {

    /** The domains the screen loads images from. */
    val imageDomains: List<String> = listOf("raw.githubusercontent.com")

    val text: String =
        """
        A paragraph with **bold**, *italic*, ~~struck~~ and `code`, and a [link](https://example.com/).

        A second paragraph, with a line break\
        in the middle of it.
        """.trimIndent()

    val blocks: String =
        """
        ## A heading

        - A list item
        - Another, with a nested list
          1. First
          2. Second

        > A quote.

        ```kotlin
        val answer = 42
        ```

        ---

        | Key | Action |
        |---|---|
        | Enter | Confirm |
        | Escape | Cancel |
        """.trimIndent()

    val alerts: String =
        """
        > [!NOTE]
        > An alert with a note.

        > [!TIP]
        > An alert with a tip.

        > [!IMPORTANT]
        > An alert that is important.

        > [!WARNING]
        > An alert with a warning.

        > [!CAUTION]
        > An alert with a caution.
        """.trimIndent()

    val images: String =
        """
        An image from a domain that is allowed is loaded:

        ![The Kepko banner](https://raw.githubusercontent.com/yasan-org/kepko/main/assets/banner.png)

        An image from any other domain shows its description: ![A picture](https://example.com/picture.png).
        """.trimIndent()
}
