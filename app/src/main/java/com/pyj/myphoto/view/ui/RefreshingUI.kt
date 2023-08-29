package com.pyj.myphoto.view.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RefreshingUI() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}