package com.pyj.myphoto.view.ui

import androidx.compose.material.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.Switch
import androidx.compose.material3.MaterialTheme

@Composable
fun SwitchView(
    checked: Boolean,
    enabledSwitch: Boolean,
    modifier: Modifier = Modifier,
    onCheckedChange: (Boolean) -> Unit
) {
    val checkedThumbColor = MaterialTheme.colorScheme.primary
    val checkedTrackColor = checkedThumbColor.copy(alpha = 0.54f)
    val uncheckedThumbColor = MaterialTheme.colorScheme.onPrimary
    val uncheckedTrackColor = uncheckedThumbColor.copy(alpha = 0.38f)

    val colors = CustomSwitchColors(
        checkedThumbColor = checkedThumbColor,
        checkedTrackColor = checkedTrackColor,
        uncheckedThumbColor = uncheckedThumbColor,
        uncheckedTrackColor = uncheckedTrackColor,
        disabledCheckedThumbColor = checkedThumbColor,
        disabledCheckedTrackColor = checkedTrackColor,
        disabledUncheckedThumbColor = uncheckedThumbColor,
        disabledUncheckedTrackColor = uncheckedTrackColor
    )

    Switch(
        modifier = modifier,
        checked = checked,
        enabled = enabledSwitch,
        onCheckedChange = onCheckedChange,
        colors = colors
    )
}

@Immutable
private class CustomSwitchColors(
    private val checkedThumbColor: Color,
    private val checkedTrackColor: Color,
    private val uncheckedThumbColor: Color,
    private val uncheckedTrackColor: Color,
    private val disabledCheckedThumbColor: Color,
    private val disabledCheckedTrackColor: Color,
    private val disabledUncheckedThumbColor: Color,
    private val disabledUncheckedTrackColor: Color
) : SwitchColors {
    @Composable
    override fun thumbColor(enabled: Boolean, checked: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                if (checked) checkedThumbColor else uncheckedThumbColor
            } else {
                if (checked) disabledCheckedThumbColor else disabledUncheckedThumbColor
            }
        )
    }

    @Composable
    override fun trackColor(enabled: Boolean, checked: Boolean): State<Color> {
        return rememberUpdatedState(
            if (enabled) {
                if (checked) checkedTrackColor else uncheckedTrackColor
            } else {
                if (checked) disabledCheckedTrackColor else disabledUncheckedTrackColor
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CustomSwitchColors

        if (checkedThumbColor != other.checkedThumbColor) return false
        if (checkedTrackColor != other.checkedTrackColor) return false
        if (uncheckedThumbColor != other.uncheckedThumbColor) return false
        if (uncheckedTrackColor != other.uncheckedTrackColor) return false
        if (disabledCheckedThumbColor != other.disabledCheckedThumbColor) return false
        if (disabledCheckedTrackColor != other.disabledCheckedTrackColor) return false
        if (disabledUncheckedThumbColor != other.disabledUncheckedThumbColor) return false
        if (disabledUncheckedTrackColor != other.disabledUncheckedTrackColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = checkedThumbColor.hashCode()
        result = 31 * result + checkedTrackColor.hashCode()
        result = 31 * result + uncheckedThumbColor.hashCode()
        result = 31 * result + uncheckedTrackColor.hashCode()
        result = 31 * result + disabledCheckedThumbColor.hashCode()
        result = 31 * result + disabledCheckedTrackColor.hashCode()
        result = 31 * result + disabledUncheckedThumbColor.hashCode()
        result = 31 * result + disabledUncheckedTrackColor.hashCode()
        return result
    }
}