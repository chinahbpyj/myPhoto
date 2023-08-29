package com.pyj.myphoto.view.ui.px

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pyj.myphoto.MainActivity
import com.pyj.myphoto.data.PxPhoto
import com.pyj.myphoto.data.px.PxRepository
import com.pyj.myphoto.util.LifecycleEffect
import com.pyj.myphoto.view.ui.EmptyLayout
import com.pyj.myphoto.view.ui.LoadMoreUI
import com.pyj.myphoto.view.ui.LoadingUI
import com.pyj.myphoto.view.ui.PhotoItem
import com.pyj.myphoto.view.ui.RefreshingUI
import com.pyj.myphoto.view.ui.SwipeRefresh
import com.pyj.myphoto.view.ui.SwipeStaggeredGridRefresh
import com.pyj.myphoto.view.ui.TopAppBar
import com.pyj.myphoto.view.ui.dialog.PxSettingsDialog
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.px.PxViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PxCurated(
    pxRepository: PxRepository,
    openDrawer: () -> Unit = {},
    click: (PxPhoto) -> Unit
) {
    val appUiState: AppUiState = (LocalContext.current as MainActivity).globalUiState

    val viewModel: PxViewModel = viewModel(
        factory = PxViewModel.provideFactory(pxRepository)
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val settingUiState by viewModel.settingUiState.collectAsStateWithLifecycle()

    LifecycleEffect(
        onResume = {},
        onPause = {},
        onStop = {}
    )

    DisposableEffect(true) {
        if (uiState.request) {
            viewModel.request(settingUiState.valueName)
        }

        onDispose {}
    }

    if (uiState.request) {
        LoadingUI()
    } else {
        var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

        if (showSettingsDialog) {
            PxSettingsDialog(
                setting = settingUiState,
                typeClick = {
                    viewModel.settingUpdateType(it)
                },
                valueClick = { key, value ->
                    viewModel.settingUpdateValue(key, value)
                },
                onDismiss = {
                    showSettingsDialog = false
                    viewModel.refresh(settingUiState.valueName)
                },
            )
        }

        Column {
            TopAppBar(
                title = "500 PX",
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            imageVector = Icons.Rounded.List,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showSettingsDialog = true }) {
                        Icon(
                            imageVector = Icons.Rounded.Settings,
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )

            if (appUiState.userData?.row == true) {
                SwipeStaggeredGridRefresh(
                    refreshingUI = { RefreshingUI() },
                    loadMoreUI = { LoadMoreUI() },
                    emptyLayout = { EmptyLayout { viewModel.request(settingUiState.valueName) } },
                    items = uiState.photos,
                    refreshing = uiState.refreshing,
                    onRefresh = { viewModel.refresh(settingUiState.valueName) },
                    loading = uiState.loading,
                    onLoad = { viewModel.loadMore(settingUiState.valueName) },
                    itemContent = { _, item ->
                        PhotoItem(item, imgUrl = item.url.p2, click = click)
                    })
            } else {
                SwipeRefresh(
                    refreshingUI = { RefreshingUI() },
                    loadMoreUI = { LoadMoreUI() },
                    emptyLayout = { EmptyLayout { viewModel.request(settingUiState.valueName) } },
                    items = uiState.photos,
                    refreshing = uiState.refreshing,
                    onRefresh = { viewModel.refresh(settingUiState.valueName) },
                    loading = uiState.loading,
                    onLoad = { viewModel.loadMore(settingUiState.valueName) },
                    itemContent = { _, item ->
                        PhotoItem(item, imgUrl = item.url.p2, click = click)
                    })
            }
        }
    }
}
