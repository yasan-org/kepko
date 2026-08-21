<p align="center">
  <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/assets/banner.png" alt="Banner"/>
</p>

<p align="center">
  <a href="https://github.com/yasanglass/kepko/actions/workflows/ci.yml"><img alt="CI" src="https://github.com/yasanglass/kepko/actions/workflows/ci.yml/badge.svg"/></a>
  <a href="https://central.sonatype.com/artifact/glass.yasan.kepko/foundation"><img alt="version" src="https://img.shields.io/maven-central/v/glass.yasan.kepko/foundation?label=version"/></a>
  <a href="https://github.com/yasanglass/kepko/blob/main/LICENSE"><img alt="License" src="https://img.shields.io/github/license/yasanglass/kepko"/></a>
  <a href="https://crowdin.com/project/kepko"><img alt="Crowdin" src="https://badges.crowdin.net/kepko/localized.svg"/></a>
  <a href="https://codecov.io/gh/yasanglass/kepko"><img alt="Codecov" src="https://codecov.io/gh/yasanglass/kepko/graph/badge.svg"/></a>
  <a href="https://context7.com/yasanglass/kepko/llms.txt"><img alt="Context7" src="https://img.shields.io/badge/context7-llms.txt-blue"/></a>
  <a href="https://deepwiki.com/yasanglass/kepko"><img alt="DeepWiki" src="https://deepwiki.com/badge.svg"/></a>
  <a href="https://yasanglass.github.io/kepko/"><img alt="Documentation" src="https://img.shields.io/badge/documentation-blue"/></a>
</p>

An opinionated design system for Compose Multiplatform.

## Installation

Published on [Maven Central](https://central.sonatype.com/namespace/glass.yasan.kepko):

```kotlin
implementation("glass.yasan.kepko:component:<version>")
implementation("glass.yasan.kepko:foundation:<version>")
```

## Usage

Wrap your app content with [`KepkoTheme`](https://github.com/yasanglass/kepko/blob/main/foundation/src/commonMain/kotlin/glass/yasan/kepko/foundation/theme/Theme.kt):

| Code | Preview |
|---|---|
| <pre lang="kotlin">KepkoTheme {&#10;    Text("Hello, Kepko!")&#10;    TextPill(&#10;        text = "Yasan Glass",&#10;        containerColor = KepkoTheme.colors.information,&#10;    )&#10;}</pre> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/UsageLightReadmePreview.png" alt="Usage" width="300"/> |

## Components

| Light | Dark |
|---|---|
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ButtonLightReadmePreview.png" alt="Button Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ButtonDarkReadmePreview.png" alt="Button Dark" width="300"/> |
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/OutlinedTextFieldLightReadmePreview.png" alt="OutlinedTextField Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/OutlinedTextFieldDarkReadmePreview.png" alt="OutlinedTextField Dark" width="300"/> |
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceCheckboxLightReadmePreview.png" alt="PreferenceCheckbox Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceCheckboxDarkReadmePreview.png" alt="PreferenceCheckbox Dark" width="300"/> |
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceRadioButtonLightReadmePreview.png" alt="PreferenceRadioButton Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceRadioButtonDarkReadmePreview.png" alt="PreferenceRadioButton Dark" width="300"/> |
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceRadioGroupLightReadmePreview.png" alt="PreferenceRadioGroup Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceRadioGroupDarkReadmePreview.png" alt="PreferenceRadioGroup Dark" width="300"/> |
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceSliderLightReadmePreview.png" alt="PreferenceSlider Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceSliderDarkReadmePreview.png" alt="PreferenceSlider Dark" width="300"/> |
| <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceSwitchLightReadmePreview.png" alt="PreferenceSwitch Light" width="300"/> | <img src="https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PreferenceSwitchDarkReadmePreview.png" alt="PreferenceSwitch Dark" width="300"/> |

See the full list of components [here](https://github.com/yasanglass/kepko/tree/main/component/src/commonMain/kotlin/glass/yasan/kepko/component).

## Color Palettes

| Name | Color Palette |
|---|---|
| Light | ![Light](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteLightReadmePreview.png) |
| Dark | ![Dark](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteDarkReadmePreview.png) |
| Black (AMOLED) | ![Black (AMOLED)](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteBlackReadmePreview.png) |
| Solarized Light | ![Solarized Light](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteSolarizedLightReadmePreview.png) |
| Solarized Dark | ![Solarized Dark](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteSolarizedDarkReadmePreview.png) |
| Catppuccin Latte | ![Catppuccin Latte](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteCatppuccinLatteReadmePreview.png) |
| Catppuccin Frappé | ![Catppuccin Frappé](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteCatppuccinFrappeReadmePreview.png) |
| Catppuccin Macchiato | ![Catppuccin Macchiato](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteCatppuccinMacchiatoReadmePreview.png) |
| Catppuccin Mocha | ![Catppuccin Mocha](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteCatppuccinMochaReadmePreview.png) |
| Gruvbox Light | ![Gruvbox Light](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteGruvboxLightReadmePreview.png) |
| Gruvbox Dark | ![Gruvbox Dark](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/ColorPaletteGruvboxDarkReadmePreview.png) |

## Persistence

An optional module that automatically persists and restores theme preferences across the app launches.

```kotlin
implementation("glass.yasan.kepko:persistence:<version>")
```

Use [`PersistentKepkoTheme`](https://github.com/yasanglass/kepko/blob/main/persistence/src/commonMain/kotlin/glass/yasan/kepko/persistence/PersistentKepkoTheme.kt) instead of `KepkoTheme` to automatically persist and restore theme preferences:

```kotlin
@OptIn(ExperimentalKepkoApi::class)
PersistentKepkoTheme {
    // your app content
}
```

To let users change the persisted theme setting, use [`PersistentPreferenceThemeScreen`](https://github.com/yasanglass/kepko/blob/main/persistence/src/commonMain/kotlin/glass/yasan/kepko/persistence/PersistentPreferenceThemeScreen.kt):

```kotlin
PersistentPreferenceThemeScreen(
    onBackClick = { /* navigate back */ },
)
```

`PersistentPreferenceThemeScreen` lets users pick between system-based or manual theme selection. In system mode, separate light and dark style pickers are shown:

![PersistentPreferenceThemeScreen](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/PersistentPreferenceThemeScreenReadmePreview.png)

[`PersistentKepkoTheme`](https://github.com/yasanglass/kepko/blob/main/persistence/src/commonMain/kotlin/glass/yasan/kepko/persistence/PersistentKepkoTheme.kt) provides the following [`CompositionLocal`](https://developer.android.com/reference/kotlin/androidx/compose/runtime/CompositionLocal) values inside its content:

- [`LocalKepkoColorPalette`](https://github.com/yasanglass/kepko/blob/main/persistence/src/commonMain/kotlin/glass/yasan/kepko/persistence/PersistenceManager.kt): the currently active [`ColorPalette`](https://github.com/yasanglass/kepko/blob/main/foundation/src/commonMain/kotlin/glass/yasan/kepko/foundation/theme/ColorPalette.kt)
- [`LocalKepkoPersistenceManager`](https://github.com/yasanglass/kepko/blob/main/persistence/src/commonMain/kotlin/glass/yasan/kepko/persistence/PersistenceManager.kt): the [`PersistenceManager`](https://github.com/yasanglass/kepko/blob/main/persistence/src/commonMain/kotlin/glass/yasan/kepko/persistence/PersistenceManager.kt) instance for direct access to persistence state

## Serialization

An optional experimental module that allows constructing Kepko components from JSON strings by providing serialization contracts for the components.

```kotlin
implementation("glass.yasan.kepko:serialization:<version>")
```

Simple JSON:

```json
{
  "on_click": "on-click",
  "text": "Text Value",
  "leading_icon": "info"
}
```

Result (decoded contract → composable):

```kotlin
val contract = Json(from = kepkoJson).decodeFromString<ButtonContract>(jsonString)

ContractButton(
    contract = contract,
    onClick = { action: String -> println(action) },
)
```

Equivalent to using `Button` directly:

```kotlin
Button(
    text = "Text Value",
    onClick = { println("on-click") },
    leadingIcon = NamedIcon.INFO.painter(),
)
```

## Sample Project

Explore Kepko in a real project with the included [sample project](https://github.com/yasanglass/kepko/tree/main/sample).

You can also try the sample project in your browser: **[WebAssembly](https://yasanglass.github.io/kepko/sample-wasm/)** or **[JavaScript](https://yasanglass.github.io/kepko/sample-js/)**

![Sample](https://raw.githubusercontent.com/yasanglass/kepko/main/sample/composeApp/assets/readme/SampleLightReadmePreview.png)
