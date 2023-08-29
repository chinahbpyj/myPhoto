package com.pyj.myphoto.view.ui.daily

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pyj.myphoto.MainActivity
import com.pyj.myphoto.R
import com.pyj.myphoto.data.ContentItem
import com.pyj.myphoto.view.ui.EmptyLayout
import com.pyj.myphoto.view.ui.LoadMoreUI
import com.pyj.myphoto.view.ui.LoadingUI
import com.pyj.myphoto.view.ui.RefreshingUI
import com.pyj.myphoto.view.ui.ShareButton
import com.pyj.myphoto.view.ui.SwipeRefresh
import com.pyj.myphoto.view.ui.TopAppBar
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.app.AppViewModel
import com.pyj.myphoto.viewmodel.daily.DailyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyCurated(appViewModel: AppViewModel, share: () -> Unit, onBack: () -> Unit) {
    val appUiState: AppUiState = (LocalContext.current as MainActivity).globalUiState

    val viewModel: DailyViewModel = viewModel(
        factory = DailyViewModel.provideFactory()
    )
    var contentData: String? by remember { mutableStateOf(appUiState.shareData?.content ?: "") }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val imgHeight = configuration.screenWidthDp.dp * 9 / 16

    val contentItem = ContentItem(
        src = appUiState.dailyData?.picture2 ?: "",
        content = appUiState.dailyData?.content ?: "",
        note = appUiState.dailyData?.note ?: "",
        dateline = appUiState.dailyData?.dateline ?: ""
    )

    DisposableEffect(true) {
        viewModel.request(contentItem)
        onDispose {}
    }
    if (uiState.request) {
        LoadingUI()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            TopAppBar(
                title = stringResource(id = R.string.daily),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                actions = {
                    if (contentData != null) {
                        ShareButton(
                            tint = MaterialTheme.colorScheme.onBackground,
                            onClick = share
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )

            SwipeRefresh(
                refreshingUI = { RefreshingUI() },
                loadMoreUI = { LoadMoreUI() },
                emptyLayout = { EmptyLayout { viewModel.request(contentItem) } },
                items = uiState.contents,
                limitLoadMore = false,
                limitRefresh = false,
                refreshing = false,
                onRefresh = { },
                loading = false,
                onLoad = { },
                itemContent = { _, item ->
                    val selected = if (contentData == null) {
                        false
                    } else {
                        contentData.equals(item.content)
                    }

                    ContentItem(
                        selected = selected,
                        item = item,
                        imgHeight = imgHeight,
                        onClick = {
                            contentData = it.content
                            appViewModel.contentData(it)
                        })
                })
        }
    }
}

@Composable
private fun ContentItem(
    selected: Boolean,
    item: ContentItem,
    imgHeight: Dp,
    onClick: (ContentItem) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(item)
            }
            .clip(RoundedCornerShape(10.dp))
    ) {
        val (img, textRoot, selectRoot) = createRefs()

        if (!item.src.isNullOrEmpty()) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(img) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.src)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(textRoot) {
                        bottom.linkTo(img.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .background(color = Color(0x80000000)),
            ) {
                if (!item.content.isNullOrEmpty()) {
                    Text(
                        text = item.content,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            lineHeight = 24.0.sp,
                            letterSpacing = 0.sp,
                        ),
                        color = Color(0xFFFFFFFF),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 10.dp)
                    )
                }

                if (!item.dateline.isNullOrEmpty()) {
                    Text(
                        text = item.dateline,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            lineHeight = 16.0.sp,
                            letterSpacing = 0.3.sp,
                        ),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp, end = 10.dp)
                    )
                }
            }

            if (selected) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imgHeight)
                        .constrainAs(selectRoot) {
                            top.linkTo(img.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(img.bottom)
                        }
                        .background(color = Color(0xBF000000)),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SelectCheckBox(selected = true)
                }
            }
        }
    }
}

@Composable
private fun SelectCheckBox(
    modifier: Modifier = Modifier,
    selected: Boolean = false
) {
    val icon = if (selected) Icons.Filled.Done else Icons.Filled.Add
    val iconColor = if (selected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.primary
    }
    val borderColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    }
    val backgroundColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onPrimary
    }
    Surface(
        color = backgroundColor,
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier.size(40.dp, 40.dp)
    ) {
        Image(
            imageVector = icon,
            colorFilter = ColorFilter.tint(iconColor),
            modifier = Modifier.padding(8.dp),
            contentDescription = null
        )
    }
}
