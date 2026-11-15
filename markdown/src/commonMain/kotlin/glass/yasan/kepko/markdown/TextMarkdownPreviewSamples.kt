package glass.yasan.kepko.markdown

/**
 * Markdown for the [TextMarkdown] previews: an overview, then groups that follow the CommonMark and
 * GitHub Flavored Markdown specifications, syntax from beyond them, and text that is not quite
 * markdown. The samples of a group are separated by lines of `%%`.
 *
 * A sample is here to record how it is shown, whether or not it is supported.
 */
@Suppress("LargeClass")
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

    const val HEADINGS: String =
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
        ## A heading with closing hashes ##
        %%
        ## A heading with *italic*, `code` and a [link](https://example.com/)
        %%
        ### A heading over a paragraph

        The paragraph under the heading.
        %%
        ####### Seven hashes are not a heading
        %%
        ## A heading long enough that it has to wrap onto a second line
        """

    const val PARAGRAPHS: String =
        """
        Two paragraphs in one text.

        The second one starts after a blank line and is long enough to wrap onto a second line.
        %%
        A hard line break with a backslash\
        puts this on the next line.
        %%
        A hard line break with two spaces<br>
        is written here as a tag.
        %%
        A soft line break after this line
        keeps this in the same paragraph.
        %%
        Three

        short

        paragraphs.
        %%
        Emoji and symbols: 🎉 → ✓ ½ “quotes” — dash.
        """

    const val EMPHASIS: String =
        """
        Bold with **asterisks** and __underscores__.
        %%
        Italic with *asterisks* and _underscores_.
        %%
        Bold and italic ***together***, and **bold with *italic* inside**.
        %%
        Emphasis in**side** a w*or*d, and snake_case_words left alone.
        %%
        Strikethrough with ~~two tildes~~ and ~one tilde~.
        %%
        **Bold `code`**, *italic `code`* and ~~struck `code`~~.
        """

    const val CODE: String =
        """
        Inline code such as `value` and `a_longer_name`.
        %%
        Inline code with a backtick in it: `` a ` b ``.
        %%
        A fenced code block:

        ```kotlin
        val rows = settings.rows
        println("rows = " + rows)
        ```
        %%
        A fenced block with tildes and no language:

        ~~~
        plain text in a block
        ~~~
        %%
        An indented code block with a long line:

            a line of code that is much too long to fit and so runs off the side
        %%
        Markdown inside a code block stays literal:

        ```
        **not bold** and [not a link](https://example.com/)
        ```
        """

    const val LINKS: String =
        """
        An inline link to [a page](https://example.com/) mid sentence.
        %%
        A link with a title: [a page](https://example.com/ "The title").
        %%
        A **[bold link](https://example.com/)** and a link with [`code`](https://example.com/).
        %%
        A reference link to [a page][page], a collapsed one to [page][] and a shortcut to [page].

        [page]: https://example.com/page
        %%
        Autolinks: <https://example.com/> and <someone@example.com>.
        %%
        A relative link to [a section](#section) and to [a file](docs/file.md).
        %%
        A very long link: <https://example.com/a/very/long/path/that/wraps/onto/another/line/of/text>.
        """

    const val IMAGES: String =
        """
        An image in a sentence: ![A small icon](https://example.com/icon.png) shows its description.
        %%
        An image on its own line:

        ![A wide picture](https://example.com/picture.png)
        %%
        An image with no description: ![](https://example.com/picture.png) shows nothing.
        %%
        An image with a title: ![Picture](https://example.com/picture.png "The title").
        %%
        A linked image: [![Picture](https://example.com/picture.png)](https://example.com/).
        %%
        A reference image: ![Picture][picture].

        [picture]: https://example.com/picture.png
        """

    const val LISTS: String =
        """
        A bullet list:

        - Red
        - Green
        - Blue
        %%
        Other bullet markers:

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
        A numbered list with parentheses:

        1) First
        2) Second
        %%
        A loose list, with blank lines between items:

        - One

        - Two

        - Three
        %%
        - Text that is only a list
        - with two items
        """

    const val LIST_ITEMS: String =
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
        Items with formatting and long text:

        - **Bold**: an item long enough to wrap onto a second line, and then a little further still.
        - *Italic*: with [a link](https://example.com/).
        - `Code` at the start of an item.
        %%
        An item with paragraphs:

        - First paragraph of the item.

          Second paragraph of the same item.
        - Next item.
        %%
        A list between paragraphs:

        - One
        - Two

        The paragraph after the list.
        %%
        An item with a quote and code:

        - Item

          > Quoted in the item.

          ```
          code in the item
          ```
        """

    const val BLOCK_QUOTES: String =
        """
        > A block quote.
        > It runs over two source lines.
        %%
        > A quote whose second line is lazy,
        with no marker in front of it.
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
        > ### A quote with a heading
        >
        > - and a list
        > - of two items
        >
        > ```
        > and code
        > ```
        """

    const val THEMATIC_BREAKS: String =
        """
        A rule of dashes:

        ---

        After the rule.
        %%
        A rule of stars:

        ***

        After the rule.
        %%
        A rule of underscores:

        ___

        After the rule.
        """

    const val ESCAPES: String =
        """
        Escaped characters stay literal: \*not italic\*, \`not code\`, \# not a heading, 2 \* 3 = 6.
        %%
        An escaped link: \[not a link\](https://example.com/).
        %%
        Named entities: &copy; &amp; &lt;tag&gt; &nbsp; &hearts;.
        %%
        Numeric entities: &#169; and &#x1F600;.
        """

    const val HTML: String =
        """
        Inline <b>bold</b>, <i>italic</i> and <code>code</code> tags.
        %%
        Inline <kbd>Ctrl</kbd> + <kbd>C</kbd>, H<sub>2</sub>O and x<sup>2</sup>.
        %%
        An HTML block:

        <details><summary>More</summary>Hidden text.</details>
        %%
        A comment <!-- that is hidden --> in a sentence.
        """

    const val TABLES: String =
        """
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
        %%
        A table with an escaped pipe and an empty cell:

        | Name | Value |
        |---|---|
        | a \| b | |
        """

    const val GITHUB_EXTENSIONS: String =
        """
        A task list:

        - [x] Done
        - [x] Also done
        - [ ] Not done
        %%
        Bare addresses: https://example.com/a/page/, www.example.com and someone@example.com.
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
        """

    const val BEYOND: String =
        """
        An emoji shortcode: :tada: and :smile:.
        %%
        A mention of @someone and a reference to #123.
        %%
        Highlighted ==text== and inserted ++text++.
        %%
        Subscript H~2~O and superscript x^2^.
        %%
        Inline math ${'$'}a^2 + b^2 = c^2${'$'} and a block:

        ${'$'}${'$'}
        e = mc^2
        ${'$'}${'$'}
        %%
        A definition list:

        Term
        : The definition of the term.
        %%
        A diagram:

        ```mermaid
        graph TD; A-->B;
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
        Stars * between * words stay literal.
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
