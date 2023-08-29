package com.pyj.myphoto.view.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.BugReport
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmptyLayout(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Rounded.BugReport,
                contentDescription = null,
            )
            Spacer(Modifier.size(10.dp))
            Button(
                onClick = onClick,
                shape = RoundedCornerShape(10),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colorScheme.background),
            ) {
                Text(
                    text = "重试",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}