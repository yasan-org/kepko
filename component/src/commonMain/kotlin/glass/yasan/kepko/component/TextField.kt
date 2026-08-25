package glass.yasan.kepko.component

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.border.border
import glass.yasan.kepko.foundation.color.ProvideLocalContentColor
import glass.yasan.kepko.foundation.theme.KepkoTheme
import org.jetbrains.compose.resources.painterResource
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextField as Material3TextField
import androidx.compose.material3.TextFieldDefaults as Material3TextFieldDefaults

@ExperimentalKepkoApi
@Composable
public fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    isError: Boolean = false,
    label: String? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = KepkoTheme.shapes.extraLarge,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    interactionSource: MutableInteractionSource? = null,
) {
    val contentColor = if (enabled) KepkoTheme.colors.content else KepkoTheme.colors.contentDisabled

    ProvideLocalContentColor(color = contentColor) {
        Material3TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.border(shape = shape),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            isError = isError,
            label = label?.let {
                {
                    Text(
                        text = it,
                        fontWeight = Medium,
                    )
                }
            },
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            shape = shape,
            interactionSource = interactionSource,
            colors = colors,
        )
    }
}

public object TextFieldDefaults {

    @Composable
    public fun colors(
        focusedContainerColor: Color = KepkoTheme.colors.foreground,
        unfocusedContainerColor: Color = KepkoTheme.colors.foreground,
        disabledContainerColor: Color = KepkoTheme.colors.foreground,
        errorContainerColor: Color = KepkoTheme.colors.foreground,
        focusedTextColor: Color = KepkoTheme.colors.content,
        unfocusedTextColor: Color = KepkoTheme.colors.content,
        disabledTextColor: Color = KepkoTheme.colors.contentDisabled,
        errorTextColor: Color = KepkoTheme.colors.content,
        cursorColor: Color = KepkoTheme.colors.content,
        errorCursorColor: Color = KepkoTheme.colors.danger,
        focusedIndicatorColor: Color = Color.Transparent,
        unfocusedIndicatorColor: Color = Color.Transparent,
        disabledIndicatorColor: Color = Color.Transparent,
        errorIndicatorColor: Color = Color.Transparent,
        focusedLabelColor: Color = KepkoTheme.colors.content,
        unfocusedLabelColor: Color = KepkoTheme.colors.contentSubtle,
        disabledLabelColor: Color = KepkoTheme.colors.contentDisabled,
        errorLabelColor: Color = KepkoTheme.colors.danger,
        focusedPlaceholderColor: Color = KepkoTheme.colors.contentSubtle,
        unfocusedPlaceholderColor: Color = KepkoTheme.colors.contentSubtle,
        disabledPlaceholderColor: Color = KepkoTheme.colors.contentDisabled,
        errorPlaceholderColor: Color = KepkoTheme.colors.contentSubtle,
        focusedLeadingIconColor: Color = KepkoTheme.colors.content,
        unfocusedLeadingIconColor: Color = KepkoTheme.colors.contentSubtle,
        disabledLeadingIconColor: Color = KepkoTheme.colors.contentDisabled,
        errorLeadingIconColor: Color = KepkoTheme.colors.danger,
        focusedTrailingIconColor: Color = KepkoTheme.colors.content,
        unfocusedTrailingIconColor: Color = KepkoTheme.colors.contentSubtle,
        disabledTrailingIconColor: Color = KepkoTheme.colors.contentDisabled,
        errorTrailingIconColor: Color = KepkoTheme.colors.danger,
        focusedPrefixColor: Color = KepkoTheme.colors.content,
        unfocusedPrefixColor: Color = KepkoTheme.colors.contentSubtle,
        disabledPrefixColor: Color = KepkoTheme.colors.contentDisabled,
        errorPrefixColor: Color = KepkoTheme.colors.content,
        focusedSuffixColor: Color = KepkoTheme.colors.content,
        unfocusedSuffixColor: Color = KepkoTheme.colors.contentSubtle,
        disabledSuffixColor: Color = KepkoTheme.colors.contentDisabled,
        errorSuffixColor: Color = KepkoTheme.colors.content,
    ): TextFieldColors = Material3TextFieldDefaults.colors(
        focusedContainerColor = focusedContainerColor,
        unfocusedContainerColor = unfocusedContainerColor,
        disabledContainerColor = disabledContainerColor,
        errorContainerColor = errorContainerColor,
        focusedTextColor = focusedTextColor,
        unfocusedTextColor = unfocusedTextColor,
        disabledTextColor = disabledTextColor,
        errorTextColor = errorTextColor,
        cursorColor = cursorColor,
        errorCursorColor = errorCursorColor,
        focusedIndicatorColor = focusedIndicatorColor,
        unfocusedIndicatorColor = unfocusedIndicatorColor,
        disabledIndicatorColor = disabledIndicatorColor,
        errorIndicatorColor = errorIndicatorColor,
        focusedLabelColor = focusedLabelColor,
        unfocusedLabelColor = unfocusedLabelColor,
        disabledLabelColor = disabledLabelColor,
        errorLabelColor = errorLabelColor,
        focusedPlaceholderColor = focusedPlaceholderColor,
        unfocusedPlaceholderColor = unfocusedPlaceholderColor,
        disabledPlaceholderColor = disabledPlaceholderColor,
        errorPlaceholderColor = errorPlaceholderColor,
        focusedLeadingIconColor = focusedLeadingIconColor,
        unfocusedLeadingIconColor = unfocusedLeadingIconColor,
        disabledLeadingIconColor = disabledLeadingIconColor,
        errorLeadingIconColor = errorLeadingIconColor,
        focusedTrailingIconColor = focusedTrailingIconColor,
        unfocusedTrailingIconColor = unfocusedTrailingIconColor,
        disabledTrailingIconColor = disabledTrailingIconColor,
        errorTrailingIconColor = errorTrailingIconColor,
        focusedPrefixColor = focusedPrefixColor,
        unfocusedPrefixColor = unfocusedPrefixColor,
        disabledPrefixColor = disabledPrefixColor,
        errorPrefixColor = errorPrefixColor,
        focusedSuffixColor = focusedSuffixColor,
        unfocusedSuffixColor = unfocusedSuffixColor,
        disabledSuffixColor = disabledSuffixColor,
        errorSuffixColor = errorSuffixColor,
    )
}

@PreviewWithTest
@Composable
internal fun TextFieldLightPreview() {
    KepkoTheme(palette = LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun TextFieldDarkPreview() {
    KepkoTheme(palette = DARK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun TextFieldBlackPreview() {
    KepkoTheme(palette = BLACK) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun TextFieldSolarizedLightPreview() {
    KepkoTheme(palette = SOLARIZED_LIGHT) { PreviewContent() }
}

@PreviewWithTest
@Composable
internal fun TextFieldSolarizedDarkPreview() {
    KepkoTheme(palette = SOLARIZED_DARK) { PreviewContent() }
}

@Suppress("LongMethod")
@Composable
private fun PreviewContent() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(KepkoTheme.colors.midground)
            .padding(16.dp),
    ) {
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(text = "Placeholder") },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "Filled text",
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "With label",
            onValueChange = {},
            label = "Label",
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "Disabled",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "Read-only",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "Error state",
            onValueChange = {},
            isError = true,
            label = "Error",
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "With leading icon",
            onValueChange = {},
            leadingIcon = { Icon(painter = painterResource(Res.drawable.ic_asterisk), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "With trailing icon",
            onValueChange = {},
            trailingIcon = { Icon(painter = painterResource(Res.drawable.ic_asterisk), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
        TextField(
            value = "Multiline text field\nwith multiple lines\nof content",
            onValueChange = {},
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
