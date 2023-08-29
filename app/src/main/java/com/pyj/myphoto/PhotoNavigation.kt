package com.pyj.myphoto

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

object PhotoDestinations {
    const val PX_ROUTE = "Px"
    const val YOU_ROUTE = "You"
    const val SETTING_ROUTE = "Setting"

    const val DETAIL_ROUTE = "detail/{title}"

    const val SHARE_ROUTE = "share"
    const val DAILY_ROUTE = "daily"
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(navController: NavHostController) {
    val navigateToPx: () -> Unit = {
        navController.navigate(PhotoDestinations.PX_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToYou: () -> Unit = {
        navController.navigate(PhotoDestinations.YOU_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToSetting: () -> Unit = {
        navController.navigate(PhotoDestinations.SETTING_ROUTE) {
            /*popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }*/
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToDetail: (String) -> Unit = {
        navController.navigate("detail/$it") {
            /*popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }*/
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToShare: () -> Unit = {
        navController.navigate(PhotoDestinations.SHARE_ROUTE) {
            /* popUpTo(navController.graph.findStartDestination().id) {
                 saveState = true
             }*/
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToDaily: () -> Unit = {
        navController.navigate(PhotoDestinations.DAILY_ROUTE) {
            /* popUpTo(navController.graph.findStartDestination().id) {
                 saveState = true
             }*/
            launchSingleTop = true
            restoreState = true
        }
    }
}
