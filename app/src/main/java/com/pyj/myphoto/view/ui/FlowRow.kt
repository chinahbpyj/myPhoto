package com.pyj.myphoto.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Flow(itemClick: (String) -> Unit, data: MutableList<String>) {
    FlowRow(modifier = Modifier.padding(5.dp)) {
        val itemModifier = Modifier
            .padding(vertical = 5.dp, horizontal = 5.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(10.dp)
            )

        data.forEach {
            Box(modifier = itemModifier) {
                TextButton(
                    onClick = {
                        itemClick(it)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground,
                    )
                ) {
                    Text(text = it)
                }
            }
        }
    }
}
