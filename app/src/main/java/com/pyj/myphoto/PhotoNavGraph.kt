package com.pyj.myphoto

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pyj.myphoto.view.ui.ImageDetail
import com.pyj.myphoto.view.ui.daily.DailyCurated
import com.pyj.myphoto.view.ui.px.PxCurated
import com.pyj.myphoto.view.ui.setting.Setting
import com.pyj.myphoto.view.ui.share.ShareDetail
import com.pyj.myphoto.view.ui.you.YouCurated
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.app.AppViewModel

@Composable
fun PhotoNavGraph(
    appViewModel: AppViewModel,
    enabledSwitch: Boolean,
    appUiState: AppUiState,
    navigationActions: NavigationActions,
    appContainer: AppContainer,
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
    pickPhoto: () -> Unit,
    navController: NavHostController, /*= rememberNavController()*/
    startDestination: String = PhotoDestinations.PX_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(PhotoDestinations.PX_ROUTE) {
            PxCurated(
                openDrawer = openDrawer,
                pxRepository = appContainer.pxRepository,
                click = {
                    appViewModel.imgUrl(it.url.p4)
                    val title = it.title.ifEmpty { " " }
                    navigationActions.navigateToDetail(title)
                })
        }

        composable(
            route = PhotoDestinations.DETAIL_ROUTE,
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { it ->
            it.arguments?.getString("title")?.let { title ->
                ImageDetail(
                    title = title,
                    imgUrl = appUiState.imgUrl,
                    onShare = {
                        if (appUiState.shareData != null) {
                            appViewModel.imgUrl(appUiState.imgUrl)
                            navigationActions.navigateToShare()
                        }
                    },
                    onBack = { navController.navigateUp() })
            }
        }

        composable(route = PhotoDestinations.YOU_ROUTE) {
            YouCurated(
                openDrawer = openDrawer,
                click = {
                    appViewModel.imgUrl(it.pic ?: "")
                    val title: String = it.desc ?: ""
                    navigationActions.navigateToDetail(title)
                })
        }

        composable(route = PhotoDestinations.SETTING_ROUTE) {
            Setting(
                appViewModel = appViewModel,
                enabledSwitch = enabledSwitch,
                appUiState = appUiState,
                onBack = { navController.navigateUp() },
                pickPhoto = pickPhoto,
                daily = { navigationActions.navigateToDaily() },
                imageDetail = {
                    appViewModel.imgUrl(it)
                    navigationActions.navigateToDetail("本地图片分享")
                }
            )
        }

        composable(
            route = PhotoDestinations.SHARE_ROUTE
        ) {
            ShareDetail(
                appUiState = appUiState,
                imgUrl = appUiState.imgUrl,
                onBack = { navController.navigateUp() })
        }

        composable(
            route = PhotoDestinations.DAILY_ROUTE
        ) {
            DailyCurated(
                appViewModel = appViewModel,
                share = {
                    navigationActions.navigateToShare()
                },
                onBack = { navController.navigateUp() })
        }
    }
}
