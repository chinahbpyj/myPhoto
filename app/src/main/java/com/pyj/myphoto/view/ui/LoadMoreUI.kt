package com.pyj.myphoto.view.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadMoreUI() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}