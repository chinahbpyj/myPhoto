package com.pyj.myphoto.view.ui

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pyj.myphoto.util.BlurTransformation
import com.pyj.myphoto.util.StoragePermission
import com.pyj.myphoto.util.saveImageToDCIM
import com.pyj.myphoto.util.saveImageToWallpaper
import com.pyj.myphoto.util.sharePost
import com.pyj.myphoto.util.shareText
import kotlinx.coroutines.launch

const val CLICK_TYPE_DOWNLOAD = 1
const val CLICK_TYPE_WALLPAPER = 2
const val CLICK_TYPE_SHARE = 3

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ImageDetail(title: String, imgUrl: String, onShare: () -> Unit, onBack: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

    var state by remember { mutableStateOf(true) }
    var drawable: Drawable? by remember { mutableStateOf(null) }
    var storagePermission by remember { mutableStateOf(false) }
    var imageUri: Uri? by remember { mutableStateOf(null) }
    val context = LocalContext.current

    var clickType = 0

    val setWallpaper = {
        if (imageUri != null) {
            saveImageToWallpaper(context, imageUri)
        } else {
            if (drawable != null) {
                coroutineScope.launch {
                    try {
                        val bd = drawable as BitmapDrawable
                        val bitmap = bd.bitmap
                        imageUri = saveImageToWallpaper(context, bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(context, "壁纸设置失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    val download = {
        if (imageUri != null) {
            Toast.makeText(context, "图片保存成功", Toast.LENGTH_SHORT).show()
        } else {
            if (drawable != null) {
                coroutineScope.launch {
                    try {
                        val bd = drawable as BitmapDrawable
                        val bitmap = bd.bitmap
                        imageUri = saveImageToDCIM(context, bitmap, true)
                    } catch (e: Exception) {
                        Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    val share = {
        if (imageUri != null) {
            sharePost(context, imageUri!!)
        } else {
            if (drawable != null) {
                coroutineScope.launch {
                    try {
                        val bd = drawable as BitmapDrawable
                        val bitmap = bd.bitmap
                        imageUri = saveImageToDCIM(context, bitmap, false)
                        if (imageUri != null) {
                            sharePost(context, imageUri!!)
                        } else {
                            Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    BackHandler {
        if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
            coroutineScope.launch {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        } else {
            onBack()
        }
    }

    BottomSheetScaffold(
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetShape = RoundedCornerShape(10.dp),
        content = {
            ModalBottomSheetLayout(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                sheetContent = {}
            ) {
                Surface {
                    ImgDetail(
                        title = title,
                        imgUrl = imgUrl,
                        onBack = {
                            if (bottomSheetScaffoldState.bottomSheetState.isExpanded) {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            } else {
                                onBack()
                            }
                        },
                        onLoading = { state = true },
                        onSuccess = { state = false },
                        wallpaper = {
                            storagePermission = true
                            clickType = CLICK_TYPE_WALLPAPER
                        },
                        downLoad = {
                            storagePermission = true
                            clickType = CLICK_TYPE_DOWNLOAD
                        },
                        share = {
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
                        },
                        requestImage = {
                            Image(imgUrl,
                                drawable = {
                                    drawable = it
                                },
                                onLoading = { state = true },
                                onSuccess = { state = false })
                        })

                    if (state) {
                        LoadingUI()
                    }

                    if (storagePermission) {
                        StoragePermission(
                            granted = {
                                storagePermission = false

                                when (clickType) {
                                    CLICK_TYPE_WALLPAPER -> setWallpaper()

                                    CLICK_TYPE_DOWNLOAD -> download()

                                    CLICK_TYPE_SHARE -> share()
                                }
                            },
                            onPermissionResult = {
                                storagePermission = false

                                if (it) {
                                    when (clickType) {
                                        CLICK_TYPE_WALLPAPER -> setWallpaper()

                                        CLICK_TYPE_DOWNLOAD -> download()

                                        CLICK_TYPE_SHARE -> share()
                                    }
                                }
                            })
                    }
                }
            }
        },
        sheetContent = {
            ShareBottomSheet(
                onImageShare = {
                    storagePermission = true
                    clickType = CLICK_TYPE_SHARE

                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                },
                onImageTextShare = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }

                    onShare()
                },
                onTextShare = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }

                    shareText(context, imgUrl)
                }
            )
        },
        scaffoldState = bottomSheetScaffoldState,
        sheetPeekHeight = 0.dp
    )
}

@Composable
private fun ShareBottomSheet(
    onImageShare: () -> Unit,
    onImageTextShare: () -> Unit,
    onTextShare: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "分享",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                lineHeight = 16.0.sp,
                letterSpacing = 0.3.sp,
            ),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "图片",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 24.0.sp,
                letterSpacing = 0.sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(300.dp)
                .clickable { onImageShare() }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(vertical = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "海报",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 24.0.sp,
                letterSpacing = 0.sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(300.dp)
                .clickable { onImageTextShare() }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(vertical = 20.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "文本",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                lineHeight = 24.0.sp,
                letterSpacing = 0.sp,
            ),
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(300.dp)
                .clickable { onTextShare() }
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(vertical = 20.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.safeDrawing))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("InvalidColorHexValue")
@Composable
private fun ImgDetail(
    title: String,
    imgUrl: String,
    onBack: () -> Unit,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    wallpaper: () -> Unit,
    downLoad: () -> Unit,
    share: () -> Unit,
    requestImage: @Composable () -> Unit
) {
    Column {
        Surface(modifier = Modifier.fillMaxSize()) {
            DetailContent(
                imgUrl = imgUrl,
                onLoading = onLoading,
                onSuccess = onSuccess,
                requestImage = requestImage
            )

            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            maxLines = 1,
                            softWrap = false,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFFFFFFFF),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(start = 5.dp, end = 10.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null,
                                tint = Color(0xFFFFFFFF)
                            )
                        }
                    },
                    actions = {
                        DownLoadButton(
                            tint = Color(0xFFFFFFFF),
                            onClick = downLoad
                        )

                        WallpaperButton(
                            tint = Color(0xFFFFFFFF),
                            onClick = wallpaper
                        )

                        ShareButton(
                            tint = Color(0xFFFFFFFF),
                            onClick = share
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0x00FFFFFF),
                    )
                )
            }
        }
    }
}

@Composable
private fun DetailContent(
    imgUrl: String?,
    modifier: Modifier = Modifier,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    requestImage: @Composable () -> Unit
) {
    Surface {
        AsyncImage(
            // placeholder = rememberAsyncImagePainter(imgUrl),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .transformations(
                    BlurTransformation(
                        LocalContext.current,
                        radius = 25f,
                        sampling = 5f
                    )
                )
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
            onLoading = {
                onLoading()
            },
            onSuccess = {
                onSuccess()
            }
        )

        requestImage()
    }
}

@Composable
private fun Image(
    imgUrl: String,
    onLoading: () -> Unit,
    onSuccess: () -> Unit,
    drawable: (Drawable) -> Unit
) {
    /**
     * 缩放比例
     */
    var scale by remember { mutableStateOf(1f) }

    /**
     * 监听手势状态变换
     */
    val state = rememberTransformableState(onTransformation = { zoomChange, panChange, _ ->
        scale = (zoomChange * scale).coerceAtLeast(1f)
        scale = if (scale > 5f) {
            5f
        } else {
            scale
        }
    })

    val configuration = LocalConfiguration.current

    AsyncImage(
        //placeholder = rememberAsyncImagePainter(imgUrl),
        model = ImageRequest.Builder(LocalContext.current)
            .data(imgUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .transformable(state = state)
            //.offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        scale = if (scale <= 1f) {
                            2f
                        } else {
                            1f
                        }
                    }
                )
            }
            .fillMaxSize(),
        contentScale = ContentScale.Fit,
        onLoading = {
            onLoading()
        },
        onSuccess = {
            onSuccess()
            drawable(it.result.drawable)
        }
    )
}