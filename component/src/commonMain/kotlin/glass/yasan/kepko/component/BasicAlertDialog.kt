package glass.yasan.kepko.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import glass.yasan.kepko.foundation.annotation.ExperimentalKepkoApi
import glass.yasan.kepko.foundation.system.DialogScrimEffect
import glass.yasan.kepko.foundation.system.withScrimColor
import glass.yasan.kepko.foundation.theme.KepkoTheme
import androidx.compose.material3.BasicAlertDialog as Material3BasicAlertDialog

@ExperimentalKepkoApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun BasicAlertDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit,
) {
    val scrimColor = KepkoTheme.colors.scrim

    Material3BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        properties = properties.withScrimColor(scrimColor),
    ) {
        DialogScrimEffect(scrimColor)
        content()
    }
}
