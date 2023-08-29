package com.pyj.myphoto.view.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pyj.myphoto.viewmodel.app.AppUiState

@Composable
fun ShareModel3(
    imgUrl: String,
    selected: Boolean,
    appUiState: AppUiState,
    onClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val dailyData = appUiState.shareData!!
    val screenWidth = configuration.screenWidthDp.dp
    val imgWidth = screenWidth * 0.85f

    ConstraintLayout(
        modifier = Modifier
            .selectable(
                selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .background(color = Color(0xE6FFFFFF))
    ) {
        val (img, textRoot) = createRefs()

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .allowHardware(false)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .width(imgWidth)
                .clip(RoundedCornerShape(10.dp))
                .constrainAs(img) {
                    top.linkTo(parent.top, margin = 15.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            contentScale = ContentScale.FillWidth,
            onLoading = {

            },
            onSuccess = {

            }
        )

        Column(
            modifier = Modifier
                .width(screenWidth)
                .constrainAs(textRoot) {
                    top.linkTo(img.bottom, margin = 10.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
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
