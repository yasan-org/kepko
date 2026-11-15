package glass.yasan.kepko.sample.home.markdown

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.component.Scaffold
import glass.yasan.kepko.component.Text
import glass.yasan.kepko.component.TextButton
import glass.yasan.kepko.component.TextField
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.markdown.TextMarkdown

@Composable
internal fun MarkdownScreen(
    onBackClick: () -> Unit,
) {
    var markdown by rememberSaveable { mutableStateOf(MarkdownPresets.text) }
    var lastLink by rememberSaveable { mutableStateOf<String?>(null) }

    fun applyPreset(preset: String) {
        markdown = preset
        lastLink = null
    }

    Scaffold(
        title = "Markdown",
        onBackClick = onBackClick,
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = 12.dp)
                .padding(top = 16.dp),
        ) {
            SectionTitle(
                text = "Presets",
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp),
            )
            PresetButtonsRow(
                onTextClick = { applyPreset(MarkdownPresets.text) },
                onBlocksClick = { applyPreset(MarkdownPresets.blocks) },
                onAlertsClick = { applyPreset(MarkdownPresets.alerts) },
                onImagesClick = { applyPreset(MarkdownPresets.images) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
            )

            SectionTitle(
                text = "Markdown",
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp),
            )
            TextField(
                value = markdown,
                onValueChange = { markdown = it },
                label = null,
                singleLine = false,
                minLines = 6,
                maxLines = 18,
                textStyle = TextStyle(fontSize = 12.sp, lineHeight = 14.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
            )

            ComposableSection(
                markdown = markdown,
                lastLink = lastLink,
                onLinkClick = { link -> lastLink = "on_link_click: $link" },
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ComposableSection(
    markdown: String,
    lastLink: String?,
    onLinkClick: (String) -> Unit,
) {
    SectionTitle(
        text = "Composable",
        modifier = Modifier.padding(top = 12.dp, bottom = 4.dp),
    )
    TextMarkdown(
        text = markdown,
        onLinkClick = onLinkClick,
        imageDomains = MarkdownPresets.imageDomains,
        modifier = Modifier.fillMaxWidth(),
    )
    lastLink?.let { link ->
        Text(
            text = link,
            style = TextStyle(fontSize = 10.sp, lineHeight = 12.sp),
            color = KepkoTheme.colors.contentSubtle,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 4.dp),
        )
    }
}

@Composable
private fun PresetButtonsRow(
    onTextClick: () -> Unit,
    onBlocksClick: () -> Unit,
    onAlertsClick: () -> Unit,
    onImagesClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val padding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        TextButton(
            onClick = onTextClick,
            content = { Text(text = "Text", maxLines = 1) },
            contentPadding = padding,
            modifier = Modifier.weight(1f),
        )

        TextButton(
            onClick = onBlocksClick,
            content = { Text(text = "Blocks", maxLines = 1) },
            contentPadding = padding,
            modifier = Modifier.weight(1f),
        )

        TextButton(
            onClick = onAlertsClick,
            content = { Text(text = "Alerts", maxLines = 1) },
            contentPadding = padding,
            modifier = Modifier.weight(1f),
        )

        TextButton(
            onClick = onImagesClick,
            content = { Text(text = "Images", maxLines = 1) },
            contentPadding = padding,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Bold,
        ),
        modifier = modifier.fillMaxWidth(),
    )
}
