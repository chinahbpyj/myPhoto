package com.pyj.myphoto.view.ui.share

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pyj.myphoto.R
import com.pyj.myphoto.util.ImageUtils
import com.pyj.myphoto.util.StoragePermission
import com.pyj.myphoto.util.saveImageToDCIM
import com.pyj.myphoto.util.sharePost
import com.pyj.myphoto.view.ui.HalfLoadingUI
import com.pyj.myphoto.view.ui.ShareButton
import com.pyj.myphoto.view.ui.TopAppBar
import com.pyj.myphoto.viewmodel.app.AppUiState
import kotlinx.coroutines.launch

const val MODEL_TYPE_1 = 1
const val MODEL_TYPE_2 = 2
const val MODEL_TYPE_3 = 3

@SuppressLint("CoroutineCreationDuringComposition", "MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareDetail(appUiState: AppUiState, imgUrl: String, onBack: () -> Unit) {
    val imageUriMap: HashMap<Int, Uri>? by remember { mutableStateOf(HashMap()) }
    var storagePermission by remember { mutableStateOf(false) }
    var shareClickState by remember { mutableStateOf(false) }
    var loadingState by remember { mutableStateOf(false) }
    var selectModel by remember { mutableIntStateOf(MODEL_TYPE_1) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    BackHandler {
        if (loadingState) {
            loadingState = false
        } else {
            onBack()
        }
    }

    val imageContent = @Composable {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .allowHardware(false)
                .build(),
            contentDescription = null,
            modifier = Modifier.width(configuration.screenWidthDp.dp),
            contentScale = ContentScale.FillWidth,
            onLoading = {
                loadingState = true
            },
            onSuccess = {
                loadingState = false
            }
        )
    }

    val shareModel1 = @Composable {
        ShareModel1(
            selected = selectModel == MODEL_TYPE_1,
            appUiState = appUiState,
            imageContent = imageContent,
            onClick = {
                selectModel = MODEL_TYPE_1
            }
        )
    }

    val shareModel2 = @Composable {
        ShareModel2(
            imgUrl = imgUrl,
            selected = selectModel == MODEL_TYPE_2,
            appUiState = appUiState,
            onClick = {
                selectModel = MODEL_TYPE_2
            }
        )
    }

    val shareModel3 = @Composable {
        ShareModel3(
            imgUrl = imgUrl,
            selected = selectModel == MODEL_TYPE_3,
            appUiState = appUiState,
            onClick = {
                selectModel = MODEL_TYPE_3
            }
        )
    }

    val share = {
        shareClickState = true
    }

    if (shareClickState) {
        shareClickState = false
        val imageUri = imageUriMap?.get(selectModel)
        if (imageUri != null) {
            sharePost(context, imageUri)
        } else {
            ImageUtils.ComposeUIToView(
                content = {
                    when (selectModel) {
                        MODEL_TYPE_1 -> shareModel1()

                        MODEL_TYPE_2 -> shareModel2()

                        MODEL_TYPE_3 -> shareModel3()
                    }
                },
                onLoading = {
                    loadingState = true
                },
                onSuccess = {
                    loadingState = false

                    coroutineScope.launch {
                        try {
                            val bitmap: Bitmap? = ImageUtils.generateShareImage(it)

                            if (bitmap != null) {
                                val uri = saveImageToDCIM(context, bitmap, false)
                                if (uri != null) {
                                    imageUriMap?.set(selectModel, uri)
                                    sharePost(context, uri)
                                } else {
                                    Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        TopAppBar(
            title = stringResource(id = R.string.share),
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
                ShareButton(
                    tint = MaterialTheme.colorScheme.onBackground,
                    onClick = {
                        storagePermission = true
                    }
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0x00000000),
            )
        )

        if (storagePermission) {
            StoragePermission(
                granted = {
                    storagePermission = false
                    share()
                },
                onPermissionResult = {
                    storagePermission = false

                    if (it) {
                        share()
                    }
                })
        }

        Surface(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.Top,
            ) {
                shareModel1()

                shareModel2()

                shareModel3()
            }

            if (loadingState) {
                HalfLoadingUI()
            }
        }
    }
}

