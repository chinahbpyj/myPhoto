package com.pyj.myphoto

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pyj.myphoto.ui.theme.PhotoTheme
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.app.AppViewModel
import kotlinx.coroutines.launch

@Composable
fun PhotoApp(
    appViewModel: AppViewModel,
    appUiState: AppUiState,
    enabledSwitch: Boolean,
    darkTheme: Boolean,
    appContainer: AppContainer,
    widthSizeClass: WindowWidthSizeClass,
    pickPhoto: () -> Unit
) {
    PhotoTheme(darkTheme = darkTheme) {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            NavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: PhotoDestinations.PX_ROUTE

        val isExpandedScreen = widthSizeClass == WindowWidthSizeClass.Expanded
        val sizeAwareDrawerState = rememberSizeAwareDrawerState(isExpandedScreen)

        if (appUiState.request) {
            appViewModel.daily()
        }

        ModalNavigationDrawer(
            drawerContent = {
                AppDrawer(
                    dailyData = appUiState.dailyData,
                    currentRoute = currentRoute,
                    navigateToPx = navigationActions.navigateToPx,
                    navigateToYou = navigationActions.navigateToYou,
                    navigateToSetting = navigationActions.navigateToSetting,
                    closeDrawer = { coroutineScope.launch { sizeAwareDrawerState.close() } }
                )
            },
            drawerState = sizeAwareDrawerState,
            gesturesEnabled = sizeAwareDrawerState.isOpen /*!isExpandedScreen*/
        ) {
            Row {
                PhotoNavGraph(
                    appViewModel = appViewModel,
                    enabledSwitch = enabledSwitch,
                    appUiState = appUiState,
                    navigationActions = navigationActions,
                    appContainer = appContainer,
                    navController = navController,
                    pickPhoto = pickPhoto,
                    openDrawer = { coroutineScope.launch { sizeAwareDrawerState.open() } }
                )
            }
        }
    }
}

@Composable
private fun rememberSizeAwareDrawerState(isExpandedScreen: Boolean): DrawerState {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    return if (!isExpandedScreen) {
        // If we want to allow showing the drawer, we use a real, remembered drawer
        // state defined above
        drawerState
    } else {
        // If we don't want to allow the drawer to be shown, we provide a drawer state
        // that is locked closed. This is intentionally not remembered, because we
        // don't want to keep track of any changes and always keep it closed
        DrawerState(DrawerValue.Closed)
    }
}