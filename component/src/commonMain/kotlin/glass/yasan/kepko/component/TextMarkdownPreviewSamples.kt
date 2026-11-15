package glass.yasan.kepko.component

/**
 * Markdown for the [TextMarkdown] previews, one kind of syntax to a sample. The samples of a group
 * are separated by lines of `%%`. Together they cover GitHub Flavored Markdown, what it adds on
 * top of it, and text that is not quite markdown, so the snapshots record how each is shown.
 */
internal object TextMarkdownPreviewSamples {

    val separator: Regex = Regex("^%%$", RegexOption.MULTILINE)

    const val OVERVIEW: String =
        """
        ## A heading

        A paragraph with **bold**, *italic*, ~~struck~~ and `code`, and a [link](https://example.com/).

        - A list item
        - Another, with a nested list
          1. First
          2. Second

        > A quote.

        ```
        a code block
        ```

        | Key | Value |
        |---|---|
        | One | 1 |
        """

    const val INLINE: String =
        """
        Bold with **asterisks** and __underscores__.
        %%
        Italic with *asterisks* and _underscores_.
        %%
        Bold and italic ***together***, and **bold with *italic* inside**.
        %%
        Strikethrough for ~~what went away~~.
        %%
        Inline code such as `value` and `a_longer_name`.
        %%
        An inline link to [a page](https://example.com/) mid sentence.
        %%
        A **[bold link](https://example.com/)** and a link in code `[code](https://example.com/)`.
        %%
        A bare address https://example.com/a/page/ and <https://example.org/>.
        %%
        A reference link to [a page][page].

        [page]: https://example.com/page
        %%
        Escaped characters stay literal: \*not italic\*, \`not code\`, 2 \* 3 = 6.
        %%
        A hard line break after this line\
        puts this on the next one.
        %%
        A soft line break after this line
        keeps this in the same paragraph.
        %%
        Emoji and symbols: 🎉 → ✓ ½ “quotes” — dash.
        """

    const val LISTS: String =
        """
        A bullet list:

        - Red
        - Green
        - Blue
        %%
        A bullet list with other markers:

        * Star one
        * Star two

        + Plus one
        + Plus two
        %%
        A numbered list:

        1. Open the menu
        2. Choose an item
        3. Confirm
        %%
        A numbered list that starts later:

        7. Seventh
        8. Eighth
        9. Ninth
        10. Tenth
        %%
        A task list:

        - [x] Done
        - [x] Also done
        - [ ] Not done
        %%
        - Text that is only a list
        - with two items
        %%
        1. Text that is only a numbered list
        2. with two items
        """

    const val NESTED_LISTS: String =
        """
        A nested list:

        - Fruit
          - Citrus
            - Lemon
            - Orange
          - Berries
        - Steps
          1. Wash
          2. Peel
        %%
        List items with formatting and long text:

        - **Bold**: an item long enough to wrap onto a second line, and then a little further still.
        - *Italic*: with [a link](https://example.com/).
        - `Code` at the start of an item.
        %%
        A list item with paragraphs:

        - First paragraph of the item.

          Second paragraph of the same item.
        - Next item.
        %%
        A list between paragraphs:

        - One
        - Two

        The paragraph after the list.
        %%
        A list item with a quote and code:

        - Item

          > Quoted in the item.

          ```
          code in the item
          ```
        """

    const val BLOCKS: String =
        """
        # Heading 1

        ## Heading 2

        ### Heading 3

        #### Heading 4

        ##### Heading 5

        ###### Heading 6
        %%
        Setext heading 1
        ================

        Setext heading 2
        ----------------
        %%
        ### A heading over a paragraph

        The paragraph under the heading.
        %%
        Two paragraphs in one text.

        The second one starts after a blank line and is long enough to wrap onto a second line.
        %%
        > A block quote.
        > It runs over two source lines.
        %%
        > A quote with **bold** and
        >
        > a second paragraph.
        %%
        > Nested quotes:
        >
        > > An inner quote.
        %%
        A quote under a paragraph:

        > Quoted text.
        %%
        A rule between two paragraphs:

        ---

        After the rule.
        """

    const val CODE_AND_TABLES: String =
        """
        A fenced code block:

        ```kotlin
        val rows = settings.rows
        println("rows = " + rows)
        ```
        %%
        An indented code block with a long line:

            a line of code that is much too long to fit and so runs off the side
        %%
        A table:

        | Key | Action |
        |---|---|
        | Enter | Confirm |
        | Escape | Cancel |
        %%
        A table with alignment and formatting:

        | Left | Center | Right |
        |:---|:---:|---:|
        | **bold** | `code` | 100 |
        | a longer cell | [link](https://example.com/) | 2 |
        %%
        A wide table:

        | One | Two | Three | Four | Five | Six |
        |---|---|---|---|---|---|
        | first | second | third | fourth | fifth | sixth |
        """

    const val EXTRAS: String =
        """
        An image in a sentence: ![A small icon](https://example.com/icon.png) shows its description.
        %%
        An image on its own line:

        ![A wide picture](https://example.com/picture.png)
        %%
        An image with no description: ![](https://example.com/picture.png) shows nothing.
        %%
        A linked image: [![Picture](https://example.com/picture.png)](https://example.com/).
        %%
        Inline <b>HTML</b> and <br> tags.
        %%
        An HTML block:

        <details><summary>More</summary>Hidden text.</details>
        %%
        Entities: &copy; &amp; &lt;tag&gt; &#169;.
        %%
        > [!NOTE]
        > An alert with a note.
        %%
        > [!TIP]
        > An alert with a tip.
        %%
        > [!IMPORTANT]
        > An alert that is important.
        %%
        > [!WARNING]
        > An alert with a warning.
        %%
        > [!CAUTION]
        > An alert with a caution.
        %%
        A footnote reference[^1] in a sentence.

        [^1]: The footnote text.
        %%
        A fenced block with no language:

        ```
        plain text in a block
        ```
        """

    const val EDGE_CASES: String =
        """
        Unclosed **bold stays as typed.
        %%
        Unclosed `code stays as typed.
        %%
        A broken link [text](not a url and [text] alone.
        %%
        Stars * between * words and under_scores_in_words stay literal.
        %%
        It costs ${'$'}4.99, is 100% faster, and scores 5 < 6 > 4.
        %%
        1. Text that starts with a number and a dot.
        %%
        #NotAHeading, but
        # This is one
        %%
        %%
        A very long unbroken word: Supercalifragilisticexpialidociousandthensomemorelettersthatcannotwrap.
        %%
        A very long link: <https://example.com/a/very/long/path/that/wraps/onto/another/line/of/text>.
        %%
        Everything at once: **bold**, *italic*, ~~struck~~, `code`, [link](https://example.com/).

        1. First
        2. Second
           - Nested

        > Quote

        ```
        code
        ```
        """
}
