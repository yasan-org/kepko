package glass.yasan.kepko.sample

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import glass.yasan.kepko.component.AlertDialog
import glass.yasan.kepko.component.AlertDialogDefaults
import glass.yasan.kepko.component.BasicAlertDialog
import glass.yasan.kepko.component.Button
import glass.yasan.kepko.component.CircularProgressIndicator
import glass.yasan.kepko.component.ExpandableColumn
import glass.yasan.kepko.component.HorizontalDivider
import glass.yasan.kepko.component.Icon
import glass.yasan.kepko.component.LinearProgressIndicator
import glass.yasan.kepko.component.OutlinedTextField
import glass.yasan.kepko.component.Badge
import glass.yasan.kepko.component.PreferenceAppIdentity
import glass.yasan.kepko.component.PreferenceCheckbox
import glass.yasan.kepko.component.PreferenceRadioButton
import glass.yasan.kepko.component.PreferenceRadioGroup
import glass.yasan.kepko.component.PreferenceRadioGroupItem
import glass.yasan.kepko.component.PreferenceRadioGroupPickerChipDefaults
import glass.yasan.kepko.component.PreferenceRadioGroupPickerChipDisplayMode
import glass.yasan.kepko.component.PreferenceRadioGroupPickerChip
import glass.yasan.kepko.component.PreferenceRadioGroupPicker
import glass.yasan.kepko.component.PreferenceSegmentTitle
import glass.yasan.kepko.component.PreferenceSlider
import glass.yasan.kepko.component.PreferenceSwitch
import glass.yasan.kepko.component.Scaffold
import glass.yasan.kepko.component.Text
import glass.yasan.kepko.component.TextField
import glass.yasan.kepko.component.TextPill
import glass.yasan.kepko.composeapp.generated.resources.Res
import glass.yasan.kepko.composeapp.generated.resources.app_name
import glass.yasan.kepko.composeapp.generated.resources.ic_bolt
import glass.yasan.kepko.composeapp.generated.resources.ic_eco
import glass.yasan.kepko.composeapp.generated.resources.ic_family_star
import glass.yasan.kepko.composeapp.generated.resources.ic_heart_smile
import glass.yasan.kepko.composeapp.generated.resources.ic_new_releases
import glass.yasan.kepko.foundation.border.border
import glass.yasan.kepko.foundation.color.contentColorFor
import glass.yasan.kepko.foundation.theme.KepkoTheme
import glass.yasan.kepko.persistence.PersistentPreferenceThemeButton
import glass.yasan.kepko.resource.Icons
import glass.yasan.kepko.sample.home.exampleCheckboxText
import glass.yasan.kepko.sample.home.exampleKeyValue
import glass.yasan.kepko.sample.home.exampleSegmentedPicker
import glass.yasan.kepko.sample.home.exampleSelectableChip
import glass.yasan.kepko.sample.home.exampleTextTabular
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun HomeScreen(
    onNavigateToTheme: () -> Unit,
    onNavigateToProfiles: () -> Unit,
    onNavigateToIcons: () -> Unit,
    onNavigateToSerialization: () -> Unit,
    onNavigateToTitleBar: () -> Unit,
) {
    Scaffold(
        title = stringResource(Res.string.app_name),
        textAlign = Center,
    ) { contentPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxWidth().padding(contentPadding),
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.widthIn(max = 512.dp),
            ) {
                colorPalette()
                navigationButtons(
                    onThemeClick = onNavigateToTheme,
                    onProfilesClick = onNavigateToProfiles,
                    onIconsClick = onNavigateToIcons,
                    onSerializationClick = onNavigateToSerialization,
                    onTitleBarClick = onNavigateToTitleBar,
                )
                examplePreferenceSlider()
                examplePreferenceCheckbox()
                exampleCheckboxText()
                examplePreferenceSwitch()
                examplePreferenceRadioButton()
                examplePreferenceRadioGroup()
                examplePreferenceRadioGroupPicker()
                examplePreferenceRadioGroupPickerChip()
                exampleSegmentedPicker()
                exampleSelectableChip()
                exampleTextField()
                exampleOutlinedTextField()
                exampleAlertDialog()
                exampleKeyValue()
                exampleExpandableColumn()
                exampleButton()
                exampleTextPill()
                exampleTextTabular()
                exampleProgressIndicator()
                exampleAppIdentity()
                item { Spacer(Modifier.height(128.dp)) }
            }
        }
    }
}

private fun LazyListScope.colorPalette() {
    item {
        Column {
            Spacer(Modifier.height(12.dp))
            Row {
                ColorBox(
                    containerColor = KepkoTheme.colors.success,
                    text = "Success",
                )
                ColorBox(
                    containerColor = KepkoTheme.colors.information,
                    text = "Information",
                )
            }
            Row {
                ColorBox(
                    containerColor = KepkoTheme.colors.caution,
                    text = "Caution",
                )
                ColorBox(
                    containerColor = KepkoTheme.colors.danger,
                    text = "Danger",
                )
            }
            Row {
                ColorBox(
                    containerColor = KepkoTheme.colors.foreground,
                    text = "Foreground",
                )
                ColorBox(
                    containerColor = KepkoTheme.colors.midground,
                    text = "Midground",
                )
                ColorBox(
                    containerColor = KepkoTheme.colors.background,
                    text = "Background",
                )
            }
        }
    }
}

private fun LazyListScope.navigationButtons(
    onThemeClick: () -> Unit,
    onProfilesClick: () -> Unit,
    onIconsClick: () -> Unit,
    onSerializationClick: () -> Unit,
    onTitleBarClick: () -> Unit,
) {
    item { HorizontalDivider() }
    item {
        PersistentPreferenceThemeButton(
            onClick = onThemeClick,
        )
    }
    item {
        Column {
            PreferenceSegmentTitle(title = "Customization")
            Button(
                text = "Profiles",
                description = "Switch the active profile and customize per-profile themes.",
                onClick = onProfilesClick,
                leadingIcon = Icons.person,
                trailingIcon = Icons.chevronForward,
            )
        }
    }
    item {
        Button(
            text = "Icons",
            description = "View the full icon set with snapshot previews.",
            onClick = onIconsClick,
            leadingIcon = Icons.shapes,
            trailingIcon = Icons.chevronForward,
        )
    }
    item {
        Button(
            text = "Serialization",
            description = "Inspect persisted preference payloads.",
            onClick = onSerializationClick,
            leadingIcon = Icons.code,
            trailingIcon = Icons.chevronForward,
        )
    }
    item {
        Button(
            text = "TitleBar",
            onClick = onTitleBarClick,
            leadingIcon = Icons.shapes,
            trailingIcon = Icons.chevronForward,
        )
    }
}

private fun LazyListScope.examplePreferenceRadioButton() {
    item {
        HorizontalDivider()
    }
    item {
        val radioButtonValue = rememberSaveable { mutableStateOf(true) }

        PreferenceRadioButton(
            title = "PreferenceRadioButton",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            selected = radioButtonValue.value,
            onClick = { radioButtonValue.value = radioButtonValue.value.not() },
            leadingIcon = painterResource(Res.drawable.ic_heart_smile),
            badge = Badge.new,
        )
    }
    item {
        val radioButtonValue = rememberSaveable { mutableStateOf(false) }

        PreferenceRadioButton(
            title = "PreferenceRadioButton",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            selected = radioButtonValue.value,
            onClick = { radioButtonValue.value = radioButtonValue.value.not() },
            leadingIcon = painterResource(Res.drawable.ic_family_star),
        )
    }
    item {
        val radioButtonValue = rememberSaveable { mutableStateOf(true) }

        PreferenceRadioButton(
            title = "PreferenceRadioButton",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            selected = radioButtonValue.value,
            onClick = { radioButtonValue.value = radioButtonValue.value.not() },
        )
    }
    item {
        val radioButtonValue = rememberSaveable { mutableStateOf(false) }

        PreferenceRadioButton(
            title = "PreferenceRadioButton",
            selected = radioButtonValue.value,
            onClick = { radioButtonValue.value = radioButtonValue.value.not() },
        )
    }
    item {
        PreferenceRadioButton(
            title = "PreferenceRadioButton",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            selected = true,
            onClick = {},
            enabled = false,
        )
    }
}

@Suppress("LongMethod")
private fun LazyListScope.examplePreferenceSwitch() {
    item {
        HorizontalDivider()
    }
    item {
        val switchValue = rememberSaveable { mutableStateOf(true) }

        Column {
            PreferenceSegmentTitle(title = "General")
            PreferenceSwitch(
                title = "PreferenceSwitch",
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                checked = switchValue.value,
                onCheckedChange = { switchValue.value = it },
                badge = Badge.preview,
                leadingIcon = painterResource(Res.drawable.ic_bolt)
            )
        }
    }
    item {
        val switchValue = rememberSaveable { mutableStateOf(false) }

        PreferenceSwitch(
            title = "PreferenceSwitch",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = switchValue.value,
            onCheckedChange = { switchValue.value = it },
            badge = Badge.alpha,
        )
    }
    item {
        val switchValue = rememberSaveable { mutableStateOf(true) }

        PreferenceSwitch(
            title = "PreferenceSwitch",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = switchValue.value,
            onCheckedChange = { switchValue.value = it },
        )
    }
    item {
        val switchValue = rememberSaveable { mutableStateOf(false) }

        PreferenceSwitch(
            title = "PreferenceSwitch",
            checked = switchValue.value,
            onCheckedChange = { switchValue.value = it },
        )
    }
    item {
        PreferenceSwitch(
            title = "PreferenceSwitch",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = true,
            onCheckedChange = {},
            enabled = false,
        )
    }
    item {
        PreferenceSwitch(
            title = "PreferenceSwitch",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = false,
            onCheckedChange = {},
            enabled = false,
        )
    }
}


private fun LazyListScope.examplePreferenceCheckbox() {
    item {
        HorizontalDivider()
    }
    item {
        val checkboxValue = rememberSaveable { mutableStateOf(false) }

        PreferenceCheckbox(
            title = "PreferenceCheckbox",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = checkboxValue.value,
            onCheckedChange = { checkboxValue.value = it },
            badge = Badge.beta,
        )
    }
    item {
        val checkboxValue = rememberSaveable { mutableStateOf(false) }

        PreferenceCheckbox(
            title = "PreferenceCheckbox",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = checkboxValue.value,
            onCheckedChange = { checkboxValue.value = it },
        )
    }
    item {
        val checkboxValue = rememberSaveable { mutableStateOf(true) }

        PreferenceCheckbox(
            title = "PreferenceCheckbox",
            checked = checkboxValue.value,
            onCheckedChange = { checkboxValue.value = it },
        )
    }
    item {
        PreferenceCheckbox(
            title = "PreferenceCheckbox",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            checked = true,
            onCheckedChange = {},
            enabled = false,
        )
    }
}

private fun LazyListScope.examplePreferenceSlider() {
    item { HorizontalDivider() }
    item {
        val sliderValue = rememberSaveable { mutableStateOf(0.7f) }

        PreferenceSlider(
            title = "PreferenceSlider",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            value = sliderValue.value,
            onValueChange = { sliderValue.value = it },
            steps = 9,
            badge = Badge.legacy,
        )
    }
    item {
        val sliderValue = rememberSaveable { mutableStateOf(0.5f) }

        PreferenceSlider(
            title = "PreferenceSlider",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            value = sliderValue.value,
            onValueChange = { sliderValue.value = it },
            steps = 5,
        )
    }
    item {
        val sliderValue = rememberSaveable { mutableStateOf(0.3f) }

        PreferenceSlider(
            title = "PreferenceSlider",
            value = sliderValue.value,
            onValueChange = { sliderValue.value = it },
            steps = 3,
        )
    }
    item {
        PreferenceSlider(
            title = "PreferenceSlider",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            value = 0.5f,
            onValueChange = {},
            steps = 5,
            enabled = false,
        )
    }
    item {
        val words = arrayOf("Low", "Mid", "High", "Max", "Off")
        val sliderValue = rememberSaveable { mutableStateOf(0.5f) }

        PreferenceSlider(
            title = "PreferenceSlider",
            description = "Custom value label with random words.",
            value = sliderValue.value,
            onValueChange = { sliderValue.value = it },
            steps = 4,
            transformValueLabel = { value ->
                val index = (value * (words.size - 1)).toInt().coerceIn(0, words.lastIndex)
                words[index]
            },
        )
    }
}

@Suppress("LongMethod")
private fun LazyListScope.examplePreferenceRadioGroup() {
    item { HorizontalDivider() }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                description = "A single-item group, with supporting text.",
            ) { "Item 1" },
        )
        val selected = remember { mutableStateOf(items.first()) }

        PreferenceRadioGroup(
            title = "PreferenceRadioGroup",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            selected = selected.value,
            items = items,
            onSelect = { selected.value = it },
            badge = Badge.alpha,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Fast and responsive.",
            ) { "Item 1" },
            PreferenceRadioGroupItem("item2", icon = painterResource(Res.drawable.ic_eco)) { "Item 2" },
            PreferenceRadioGroupItem(
                "item3",
                badge = Badge.new,
                icon = painterResource(Res.drawable.ic_heart_smile),
            ) { "Item 3" },
            PreferenceRadioGroupItem("item4", segment = 1) { "Item 4" },
            PreferenceRadioGroupItem("item5", badge = Badge.experimental, segment = 1) { "Item 5" },
        )
        val selected = remember { mutableStateOf(items.first()) }

        PreferenceRadioGroup(
            title = "PreferenceRadioGroup",
            description = "Segmented items with divider.",
            selected = selected.value,
            items = items,
            onSelect = { selected.value = it },
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                description = "A short description below the title.",
            ) { "Item 1" },
            PreferenceRadioGroupItem("item2") { "Item 2" },
        )
        val selected = remember { mutableStateOf(items.first()) }

        PreferenceRadioGroup(
            title = "PreferenceRadioGroup",
            selected = selected.value,
            items = items,
            onSelect = { selected.value = it },
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                description = "Descriptions also work when disabled.",
            ) { "Item 1" },
            PreferenceRadioGroupItem("item2") { "Item 2" },
        )

        PreferenceRadioGroup(
            title = "PreferenceRadioGroup",
            description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
            selected = items.first(),
            items = items,
            onSelect = {},
            enabled = false,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                description = "Enabled item.",
            ) { "Item 1" },
            PreferenceRadioGroupItem("item2", enabled = false) { "Item 2" },
            PreferenceRadioGroupItem("item3", badge = Badge.experimental) { "Item 3" },
            PreferenceRadioGroupItem("item4", badge = Badge.legacy, enabled = false) { "Item 4" },
        )
        val selected = remember { mutableStateOf(items.first()) }

        PreferenceRadioGroup(
            title = "PreferenceRadioGroup",
            description = "Per-item disabled",
            selected = selected.value,
            items = items,
            onSelect = { selected.value = it },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
private fun LazyListScope.examplePreferenceRadioGroupPicker() {
    item { HorizontalDivider() }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Fast option.",
            ) { "Item 1" },
            PreferenceRadioGroupItem(
                "item2",
                badge = Badge.new,
                icon = painterResource(Res.drawable.ic_eco),
            ) { "Item 2" },
            PreferenceRadioGroupItem("item3", icon = painterResource(Res.drawable.ic_heart_smile)) { "Item 3" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPicker(
            title = "Picker",
            description = "Basic picker with description.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            badge = Badge.beta,
            leadingIcon = Icons.settings,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                icon = painterResource(Res.drawable.ic_family_star),
                description = "Default option.",
            ) { "Item 1" },
            PreferenceRadioGroupItem(
                "item2",
                enabled = false,
                icon = painterResource(Res.drawable.ic_new_releases),
            ) { "Item 2 (Disabled)" },
            PreferenceRadioGroupItem("item3", badge = Badge.experimental) { "Item 3" },
            PreferenceRadioGroupItem("item4", badge = Badge.legacy, enabled = false) { "Item 4 (Disabled)" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPicker(
            title = "Picker (Disabled Items)",
            description = "Some items are disabled.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.warning,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                description = "Primary option.",
            ) { "Item 1" },
            PreferenceRadioGroupItem("item2") { "Item 2" },
            PreferenceRadioGroupItem("item3", segment = 1) { "Item 3" },
            PreferenceRadioGroupItem("item4", segment = 1, enabled = false) { "Item 4 (Disabled)" },
        )
        var selectedId by remember { mutableStateOf<String?>(null) }

        PreferenceRadioGroupPicker(
            title = "Picker (No Selection)",
            description = "Nothing selected initially.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.info,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "item1",
                description = "Descriptions still show in the sheet.",
            ) { "Item 1" },
            PreferenceRadioGroupItem("item2") { "Item 2" },
        )

        PreferenceRadioGroupPicker(
            title = "Picker (Disabled)",
            selectedId = "item1",
            items = items,
            onSelectId = {},
            enabled = false,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("LongMethod")
private fun LazyListScope.examplePreferenceRadioGroupPickerChip() {
    item { HorizontalDivider() }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "S" },
            PreferenceRadioGroupItem(
                "medium",
                badge = Badge.new,
                icon = painterResource(Res.drawable.ic_family_star),
            ) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Extra Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPickerChip(
            title = "Picker Chip",
            description = "Picker chip with description.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.settings,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "auto",
                icon = painterResource(Res.drawable.ic_heart_smile),
                description = "Automatically manages schedule.",
            ) { "Auto" },
            PreferenceRadioGroupItem("on") { "On" },
            PreferenceRadioGroupItem(
                "custom",
                segment = 1,
                icon = painterResource(Res.drawable.ic_new_releases),
            ) { "Custom Schedule" },
            PreferenceRadioGroupItem("off", segment = 1, enabled = false) { "Off" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPickerChip(
            title = "Picker Chip (Segmented)",
            description = "Segmented items.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.info,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "low",
                icon = painterResource(Res.drawable.ic_eco),
                description = "Lowest intensity.",
            ) { "Low" },
            PreferenceRadioGroupItem("medium") { "Medium" },
            PreferenceRadioGroupItem("high", icon = painterResource(Res.drawable.ic_bolt)) { "High" },
        )
        var selectedId by remember { mutableStateOf<String?>(null) }

        PreferenceRadioGroupPickerChip(
            title = "Chip (No Selection)",
            description = "Shows leading icon until selected.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.settings,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium", icon = painterResource(Res.drawable.ic_family_star)) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPickerChip(
            title = "Chip (Icon Only)",
            description = "Always shows icon only.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.settings,
            displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium", icon = painterResource(Res.drawable.ic_family_star)) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        Row(modifier = Modifier.fillMaxWidth()) {
            PreferenceRadioGroupPickerChip(
                title = "Chip (Reveal)",
                description = "Icon only, reveals text on change.",
                selectedId = selectedId,
                items = items,
                onSelectId = { selectedId = it },
                leadingIcon = Icons.settings,
                displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
            )
        }
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium", icon = painterResource(Res.drawable.ic_family_star)) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
        ) {
            PreferenceRadioGroupPickerChip(
                title = "Chip (Reveal, End)",
                description = "Icon only, reveals text on change. Aligned to end.",
                selectedId = selectedId,
                items = items,
                onSelectId = { selectedId = it },
                leadingIcon = Icons.settings,
                displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
            )
        }
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium") { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf("medium") }

        PreferenceRadioGroupPickerChip(
            title = "Chip (Icon Only, Mixed Icons)",
            description = "Falls back to text when the selected item has no icon.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.settings,
            displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON,
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium") { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf("small") }

        Row(modifier = Modifier.fillMaxWidth()) {
            PreferenceRadioGroupPickerChip(
                title = "Chip (Reveal, Mixed Icons)",
                description = "Select Medium to see the text fallback for an iconless item.",
                selectedId = selectedId,
                items = items,
                onSelectId = { selectedId = it },
                leadingIcon = Icons.settings,
                displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
            )
        }
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium") { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf("small") }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
        ) {
            PreferenceRadioGroupPickerChip(
                title = "Chip (Reveal, Mixed Icons, End)",
                description = "Select Medium to see the text fallback for an iconless item. Aligned to end.",
                selectedId = selectedId,
                items = items,
                onSelectId = { selectedId = it },
                leadingIcon = Icons.settings,
                displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
            )
        }
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium", icon = painterResource(Res.drawable.ic_family_star)) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPickerChip(
            title = "Chip (Custom Colors)",
            description = "Midground container with matching outline.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.settings,
            displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
            colors = PreferenceRadioGroupPickerChipDefaults.colors(
                containerColor = KepkoTheme.colors.midground,
                outlineColor = KepkoTheme.colors.midground,
            ),
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium", icon = painterResource(Res.drawable.ic_family_star)) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        Row(modifier = Modifier.fillMaxWidth()) {
            PreferenceRadioGroupPickerChip(
                title = "Chip (Custom Colors, Start)",
                description = "Midground container with matching outline. Aligned to start.",
                selectedId = selectedId,
                items = items,
                onSelectId = { selectedId = it },
                leadingIcon = Icons.settings,
                displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
                colors = PreferenceRadioGroupPickerChipDefaults.colors(
                    containerColor = KepkoTheme.colors.midground,
                    outlineColor = KepkoTheme.colors.midground,
                ),
            )
        }
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "small",
                icon = painterResource(Res.drawable.ic_bolt),
                description = "Small size.",
            ) { "Small" },
            PreferenceRadioGroupItem("medium", icon = painterResource(Res.drawable.ic_family_star)) { "Medium" },
            PreferenceRadioGroupItem("large", icon = painterResource(Res.drawable.ic_eco)) { "Large" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth(),
        ) {
            PreferenceRadioGroupPickerChip(
                title = "Chip (Custom Colors, End)",
                description = "Midground container with matching outline. Aligned to end.",
                selectedId = selectedId,
                items = items,
                onSelectId = { selectedId = it },
                leadingIcon = Icons.settings,
                displayMode = PreferenceRadioGroupPickerChipDisplayMode.ICON_WITH_TEXT_REVEAL,
                colors = PreferenceRadioGroupPickerChipDefaults.colors(
                    containerColor = KepkoTheme.colors.midground,
                    outlineColor = KepkoTheme.colors.midground,
                ),
            )
        }
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "low",
                icon = painterResource(Res.drawable.ic_eco),
                description = "Tap to pick; long press to cycle to the next option.",
            ) { "Low (long press to cycle)" },
            PreferenceRadioGroupItem(
                "medium",
                icon = painterResource(Res.drawable.ic_family_star),
            ) { "Medium (long press to cycle)" },
            PreferenceRadioGroupItem(
                "high",
                icon = painterResource(Res.drawable.ic_bolt),
            ) { "High (long press to cycle)" },
        )
        var selectedId by remember { mutableStateOf(items.first().id) }

        PreferenceRadioGroupPickerChip(
            title = "Chip (Custom Long Press)",
            description = "Long press runs a custom action — here it cycles to the next option.",
            selectedId = selectedId,
            items = items,
            onSelectId = { selectedId = it },
            leadingIcon = Icons.settings,
            onLongClick = {
                val nextIndex = (items.indexOfFirst { it.id == selectedId } + 1) % items.size
                selectedId = items[nextIndex].id
            },
            onLongClickLabel = "Cycle to next option",
        )
    }
    item {
        val items = listOf(
            PreferenceRadioGroupItem(
                "daily",
                description = "Runs every day.",
            ) { "Daily" },
            PreferenceRadioGroupItem("weekly") { "Weekly" },
        )

        PreferenceRadioGroupPickerChip(
            title = "Chip (Disabled)",
            selectedId = "daily",
            items = items,
            onSelectId = {},
            leadingIcon = Icons.warning,
            enabled = false,
        )
    }
}

@Suppress("LongMethod")
private fun LazyListScope.exampleTextField() {
    item { HorizontalDivider() }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        TextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            placeholder = { Text(text = "Placeholder") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        TextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "Label",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        TextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "With leading icon",
            leadingIcon = { Icon(painter = painterResource(Res.drawable.ic_bolt), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        TextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "With trailing icon",
            trailingIcon = { Icon(painter = painterResource(Res.drawable.ic_heart_smile), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        TextField(
            value = "Disabled",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        TextField(
            value = "Error state",
            onValueChange = {},
            isError = true,
            label = "Error",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        TextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "Centered",
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        TextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "Multiline",
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Suppress("LongMethod")
private fun LazyListScope.exampleOutlinedTextField() {
    item { HorizontalDivider() }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            placeholder = { Text(text = "Placeholder") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "Label",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "With leading icon",
            leadingIcon = { Icon(painter = painterResource(Res.drawable.ic_bolt), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "With trailing icon",
            trailingIcon = { Icon(painter = painterResource(Res.drawable.ic_heart_smile), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        OutlinedTextField(
            value = "Disabled",
            onValueChange = {},
            enabled = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        OutlinedTextField(
            value = "Error state",
            onValueChange = {},
            isError = true,
            label = "Error",
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "Centered",
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            modifier = Modifier.fillMaxWidth(),
        )
    }
    item {
        val textValue = rememberSaveable { mutableStateOf("") }

        OutlinedTextField(
            value = textValue.value,
            onValueChange = { textValue.value = it },
            label = "Multiline",
            minLines = 3,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Suppress("LongMethod")
private fun LazyListScope.exampleAlertDialog() {
    item { HorizontalDivider() }
    item {
        val dialogStyle = rememberSaveable { mutableIntStateOf(0) }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                text = "Show AlertDialog",
                onClick = { dialogStyle.intValue = 1 },
                fillWidth = false,
            )
            Button(
                text = "Show AlertDialog",
                onClick = { dialogStyle.intValue = 2 },
                fillWidth = false,
                badge = Badge(
                    text = { "Caution" },
                    containerColor = { KepkoTheme.colors.caution },
                ),
            )
            Button(
                text = "Show AlertDialog",
                onClick = { dialogStyle.intValue = 3 },
                fillWidth = false,
                badge = Badge(
                    text = { "Danger" },
                    containerColor = { KepkoTheme.colors.danger },
                ),
            )
            Button(
                text = "Show BasicAlertDialog",
                onClick = { dialogStyle.intValue = 4 },
                fillWidth = false,
            )
        }

        if (dialogStyle.intValue in 1..3) {
            val confirmLabel = when (dialogStyle.intValue) {
                1 -> "Default"
                2 -> "Caution"
                else -> "Danger"
            }

            if (dialogStyle.intValue == 1) {
                AlertDialog(
                    onDismissRequest = { dialogStyle.intValue = 0 },
                    confirmButtonText = "Confirm",
                    onConfirmClick = { dialogStyle.intValue = 0 },
                    dismissButtonText = "Dismiss",
                    title = "AlertDialog",
                    text = "Confirm button color: $confirmLabel",
                )
            } else {
                val confirmContainerColor = when (dialogStyle.intValue) {
                    2 -> KepkoTheme.colors.caution
                    else -> KepkoTheme.colors.danger
                }

                AlertDialog(
                    onDismissRequest = { dialogStyle.intValue = 0 },
                    confirmButtonText = "Confirm",
                    onConfirmClick = { dialogStyle.intValue = 0 },
                    dismissButtonText = "Dismiss",
                    title = "AlertDialog",
                    text = "Confirm button color: $confirmLabel",
                    colors = AlertDialogDefaults.colors(
                        confirmButtonContainerColor = confirmContainerColor,
                    ),
                )
            }
        }

        if (dialogStyle.intValue == 4) {
            BasicAlertDialog(
                onDismissRequest = { dialogStyle.intValue = 0 },
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .border(shape = KepkoTheme.shapes.extraLarge)
                        .background(
                            color = KepkoTheme.colors.foreground,
                            shape = KepkoTheme.shapes.extraLarge,
                        )
                        .padding(24.dp),
                ) {
                    Text(
                        text = "BasicAlertDialog",
                        fontWeight = FontWeight.Bold,
                    )
                    Text(text = "This is custom content using the minimal dialog API.")
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.align(Alignment.End),
                    ) {
                        Button(
                            text = "Dismiss",
                            onClick = { dialogStyle.intValue = 0 },
                            fillWidth = false,
                        )
                        Button(
                            text = "Confirm",
                            onClick = { dialogStyle.intValue = 0 },
                            fillWidth = false,
                            containerColor = KepkoTheme.colors.content,
                        )
                    }
                }
            }
        }
    }
}


private fun LazyListScope.exampleExpandableColumn() {
    item { HorizontalDivider() }
    item {
        var expanded by rememberSaveable { mutableStateOf(true) }

        ExpandableColumn(
            title = "ExpandableColumn",
            expanded = expanded,
            onExpandChange = { expanded = it },
            icon = Icons.build,
        ) {
            val containerColor = KepkoTheme.colors.information

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(containerColor, KepkoTheme.shapes.extraLarge)
                    .padding(16.dp),
            ) {
                Text(
                    text = "Any composable content can live here.".uppercase(),
                    textAlign = Center,
                    fontWeight = FontWeight.Bold,
                    color = contentColorFor(containerColor),
                )
            }
        }
    }
    item {
        var expanded by rememberSaveable { mutableStateOf(false) }

        ExpandableColumn(
            title = "Controlled ExpandableColumn",
            expanded = expanded,
            onExpandChange = { expanded = it },
            icon = Icons.settings,
        ) {
            val containerColor = KepkoTheme.colors.success

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(containerColor, KepkoTheme.shapes.extraLarge)
                    .padding(16.dp),
            ) {
                Text(
                    text = "Controlled expanded state.".uppercase(),
                    textAlign = Center,
                    fontWeight = FontWeight.Bold,
                    color = contentColorFor(containerColor),
                )
            }
        }
    }
}


@Suppress("LongMethod")
private fun LazyListScope.exampleButton() {
    item { HorizontalDivider() }
    item {
        val colors = listOf(
            KepkoTheme.colors.success,
            KepkoTheme.colors.information,
            KepkoTheme.colors.caution,
            KepkoTheme.colors.danger,
        )
        val leadingIcons = listOf<Painter?>(
            painterResource(Res.drawable.ic_eco),
            painterResource(Res.drawable.ic_bolt),
            null,
            null,
        )
        val trailingIcons = listOf<Painter?>(
            Icons.chevronForward,
            null,
            Icons.shapes,
            null,
        )
        var index by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(2000)
                index = (index + 1) % colors.size
            }
        }

        Button(
            text = "Cycles all icon combinations",
            leadingIcon = leadingIcons[index],
            trailingIcon = trailingIcons[index],
            containerColor = colors[index],
            onClick = {},
        )
    }
    item {
        var expanded by remember { mutableStateOf(false) }

        Button(
            text = if (expanded) "Tap to shrink" else "Tap",
            onClick = { expanded = !expanded },
            fillWidth = false,
        )
    }
    item {
        val texts = listOf("Short", "A bit longer", "Trailing only", "No icons here")
        val colors = listOf(
            KepkoTheme.colors.success,
            KepkoTheme.colors.information,
            KepkoTheme.colors.caution,
            KepkoTheme.colors.danger,
        )
        val leadingIcons = listOf<Painter?>(
            painterResource(Res.drawable.ic_eco),
            painterResource(Res.drawable.ic_family_star),
            null,
            null,
        )
        val trailingIcons = listOf<Painter?>(
            painterResource(Res.drawable.ic_bolt),
            null,
            painterResource(Res.drawable.ic_new_releases),
            null,
        )
        var index by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(1500)
                index = (index + 1) % texts.size
            }
        }

        Button(
            text = texts[index],
            leadingIcon = leadingIcons[index],
            trailingIcon = trailingIcons[index],
            onClick = {},
            containerColor = colors[index],
            fillWidth = false,
        )
    }
    item {
        Button(
            text = "Button",
            leadingIcon = Icons.chevronBackward,
            trailingIcon = painterResource(Res.drawable.ic_bolt),
            containerColor = KepkoTheme.colors.foreground,
            badge = Badge.new,
            onClick = {},
        )
    }
    item {
        Button(
            text = "Button",
            description = "Sentence-case description sits below the label.",
            leadingIcon = painterResource(Res.drawable.ic_bolt),
            trailingIcon = Icons.chevronForward,
            onClick = {},
        )
    }
    item {
        Button(
            text = "Button",
            description = "Pairs the label with quieter supporting text.",
            leadingIcon = null,
            trailingIcon = painterResource(Res.drawable.ic_new_releases),
            containerColor = KepkoTheme.colors.information,
            onClick = {},
        )
    }
    item {
        Button(
            text = "Button",
            description = "Use sparingly — descriptions add visual weight.",
            leadingIcon = painterResource(Res.drawable.ic_family_star),
            trailingIcon = null,
            containerColor = KepkoTheme.colors.caution,
            onClick = {},
        )
    }
    item {
        Button(
            text = "Button",
            leadingIcon = painterResource(Res.drawable.ic_heart_smile),
            trailingIcon = null,
            containerColor = KepkoTheme.colors.danger,
            onClick = {},
        )
    }
    item {
        Button(
            text = "Button",
            containerColor = KepkoTheme.colors.foreground,
            onClick = {},
            textAlign = TextAlign.Center,
        )
    }
    item {
        Button(
            text = "Button",
            leadingIcon = painterResource(Res.drawable.ic_new_releases),
            trailingIcon = painterResource(Res.drawable.ic_family_star),
            containerColor = KepkoTheme.colors.foreground,
            onClick = {},
            badge = Badge.alpha,
            fillWidth = false,
        )
    }
    item {
        Button(
            text = "Button",
            leadingIcon = painterResource(Res.drawable.ic_heart_smile),
            trailingIcon = null,
            containerColor = KepkoTheme.colors.foreground,
            onClick = {},
            fillWidth = false,
        )
    }
    item {
        Button(
            text = "Button",
            leadingIcon = null,
            trailingIcon = painterResource(Res.drawable.ic_eco),
            containerColor = KepkoTheme.colors.success,
            onClick = {},
            fillWidth = false,
        )
    }
    item {
        Button(
            text = "Button",
            containerColor = KepkoTheme.colors.foreground,
            onClick = {},
            fillWidth = false,
            badge = Badge.beta,
        )
    }
    item {
        Button(
            text = "Button",
            containerColor = KepkoTheme.colors.foreground,
            onClick = {},
            fillWidth = false,
        )
    }
    item {
        Button(
            text = "Button",
            leadingIcon = painterResource(Res.drawable.ic_bolt),
            trailingIcon = Icons.chevronForward,
            onClick = {},
            enabled = false,
        )
    }
    item {
        Button(
            text = null,
            leadingIcon = painterResource(Res.drawable.ic_bolt),
            trailingIcon = null,
            containerColor = KepkoTheme.colors.foreground,
            onClick = {},
            fillWidth = false,
        )
    }
}

private fun LazyListScope.exampleTextPill() {
    item { HorizontalDivider() }
    item {
        Badge.all.forEach { badge ->
            TextPill(
                badge = badge,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

private fun LazyListScope.exampleAppIdentity() {
    item { HorizontalDivider() }
    item {
        PreferenceAppIdentity(
            title = "Kepko",
            versionName = "1.0.0",
            icon = painterResource(Res.drawable.ic_heart_smile),
            extras = arrayOf("100", "beta"),
            badge = Badge.preview,
        )
    }
}

@Composable
private fun RowScope.ColorBox(
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
    text: String = "Kepko",
) {
    val animatedContainerColor by animateColorAsState(containerColor, tween(500))
    val animatedContentColor by animateColorAsState(contentColor, tween(500))

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(KepkoTheme.shapes.extraLarge)
            .weight(1f)
            .height(128.dp)
            .background(animatedContainerColor),
    ) {
        Text(
            text = text.uppercase(),
            color = animatedContentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )
    }
}

private fun LazyListScope.exampleProgressIndicator() {
    item { HorizontalDivider() }
    item {
        var progress by remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            while (true) {
                delay(50)
                progress = (progress + 0.01f) % 1f
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CircularProgressIndicator()
                CircularProgressIndicator(progress = { progress })
            }
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
