package com.pyj.myphoto.view.ui.daily

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pyj.myphoto.data.DailyData

@Composable
fun DailyDetail(dailyData: DailyData, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(dailyData.picture2)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxWidth(),
        )

        if (dailyData.content.isNotEmpty()) {
            Text(
                text = dailyData.content,
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

        if (dailyData.note.isNotEmpty()) {
            Text(
                text = dailyData.note,
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

        if (dailyData.dateline.isNotEmpty()) {
            Text(
                text = dailyData.dateline,
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
    }
}