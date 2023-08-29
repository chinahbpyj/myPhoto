package com.pyj.myphoto.view.ui.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.pyj.myphoto.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.pyj.myphoto.viewmodel.px.PxSettingUiState
import com.pyj.myphoto.viewmodel.px.TYPE_1

@Composable
fun PxSettingsDialog(
    setting: PxSettingUiState,
    typeClick: (Int) -> Unit,
    valueClick: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .widthIn(max = configuration.screenWidthDp.dp - 80.dp)
            .heightIn(max = configuration.screenHeightDp.dp * 0.7f),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = "500px API",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsPanel(
                    setting = setting,
                    typeClick = typeClick,
                    valueClick = valueClick
                )
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.dialog_button_ok),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Composable
private fun SettingsPanel(
    setting: PxSettingUiState,
    typeClick: (Int) -> Unit,
    valueClick: (String, String) -> Unit
) {
    SettingsDialogSectionTitle(text = "热门")
    Column(Modifier.selectableGroup()) {
        SettingsDialogChooserRow(
            text = "热门",
            selected = setting.type == TYPE_1,
            onClick = { typeClick(TYPE_1) },
        )
    }

    SettingsDialogSectionTitle(text = "热门的取值")
    Column(Modifier.selectableGroup()) {
        for ((index, value) in setting.keyType1.withIndex()) {
            SettingsDialogChooserRow(
                text = value,
                selected = setting.keyName == value,
                onClick = {
                    valueClick(value, setting.valueType1[index])
                },
            )
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
private fun SettingsDialogChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
