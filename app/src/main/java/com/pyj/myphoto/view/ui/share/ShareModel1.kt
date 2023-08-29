package com.pyj.myphoto.view.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pyj.myphoto.viewmodel.app.AppUiState

@Composable
fun ShareModel1(
    selected: Boolean,
    appUiState: AppUiState,
    imageContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val dailyData = appUiState.shareData!!

    Column(
        modifier = Modifier
            .selectable(
                selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .verticalScroll(rememberScrollState())
    ) {
        imageContent()

        Column(
            Modifier
                .width(configuration.screenWidthDp.dp)
                .background(color = Color(0xFFFFFFFF))
        ) {
            val content = dailyData.content ?: ""
            if (content.isNotEmpty()) {
                Text(
                    text = content,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        lineHeight = 24.0.sp,
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp, horizontal = 10.dp)
                )
            }

            val note = dailyData.note ?: ""
            if (note.isNotEmpty()) {
                Text(
                    text = note,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        lineHeight = 16.0.sp,
                        letterSpacing = 0.3.sp,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 10.dp)
                )
            }

            val dateline = dailyData.dateline ?: ""
            if (dateline.isNotEmpty()) {
                Text(
                    text = dateline,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        lineHeight = 16.0.sp,
                        letterSpacing = 0.3.sp,
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp, horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}