package com.pyj.myphoto.view.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun <T> PhotoItem(item: T, imgUrl: String, click: (T) -> Unit) {
    val configuration = LocalConfiguration.current

    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(vertical = 10.dp, horizontal = 15.dp)
            .clickable { click(item) }
            /*.shadow(
                elevation = 3.dp,
                shape = RoundedCornerShape(10.dp)
            )*/
            .clip(RoundedCornerShape(10.dp))
            .heightIn(max = configuration.screenHeightDp.dp * 0.5f),
        /* .aspectRatio(16 / 9f)*/
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        // placeholder = painterResource(R.drawable.ic_splash),
        //placeholder = rememberAsyncImagePainter(imgUrl),
        contentScale = ContentScale.FillWidth,
    )
}