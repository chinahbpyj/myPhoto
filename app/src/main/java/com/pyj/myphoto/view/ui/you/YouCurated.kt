package com.pyj.myphoto.view.ui.you

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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pyj.myphoto.MainActivity
import com.pyj.myphoto.R
import com.pyj.myphoto.data.YouPhoto
import com.pyj.myphoto.view.ui.EmptyLayout
import com.pyj.myphoto.view.ui.LoadMoreUI
import com.pyj.myphoto.view.ui.LoadingUI
import com.pyj.myphoto.view.ui.PhotoItem
import com.pyj.myphoto.view.ui.RefreshingUI
import com.pyj.myphoto.view.ui.SwipeRefresh
import com.pyj.myphoto.view.ui.SwipeStaggeredGridRefresh
import com.pyj.myphoto.view.ui.TopAppBar
import com.pyj.myphoto.view.ui.dialog.YouSettingsDialog
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.you.YouViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun YouCurated(
    click: (YouPhoto) -> Unit,
    openDrawer: () -> Unit = {}
) {
    val appUiState: AppUiState = (LocalContext.current as MainActivity).globalUiState

    val viewModel: YouViewModel = viewModel(
        factory = YouViewModel.provideFactory()
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(true) {
        if (uiState.request) {
            viewModel.request(uiState.valueName)
        }

        onDispose {}
    }

    if (uiState.request) {
        LoadingUI()
    } else {
        var showSettingsDialog by rememberSaveable { mutableStateOf(false) }

        if (showSettingsDialog) {
            YouSettingsDialog(
                setting = uiState,
                valueClick = { key, value ->
                    viewModel.settingUpdateValue(key, value)
                },
                onDismiss = {
                    showSettingsDialog = false
                    viewModel.refresh(uiState.valueName)
                },
            )
        }

        Column {
            TopAppBar(
                title = stringResource(id = R.string.you),
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
                    emptyLayout = { EmptyLayout { viewModel.request(uiState.valueName) } },
                    items = uiState.photos,
                    limitLoadMore = false,
                    refreshing = uiState.refreshing,
                    onRefresh = { viewModel.refresh(uiState.valueName) },
                    loading = false,
                    onLoad = { viewModel.loadMore() },
                    itemContent = { _, item ->
                        PhotoItem(item, imgUrl = item.pic ?: "", click = click)
                    })
            } else {
                SwipeRefresh(
                    refreshingUI = { RefreshingUI() },
                    loadMoreUI = { LoadMoreUI() },
                    emptyLayout = { EmptyLayout { viewModel.request(uiState.valueName) } },
                    items = uiState.photos,
                    limitLoadMore = false,
                    refreshing = uiState.refreshing,
                    onRefresh = { viewModel.refresh(uiState.valueName) },
                    loading = false,
                    onLoad = { viewModel.loadMore() },
                    itemContent = { _, item ->
                        PhotoItem(item, imgUrl = item.pic ?: "", click = click)
                    })
            }
        }
    }
}