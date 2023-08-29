package com.pyj.myphoto

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pyj.myphoto.data.DailyData
import com.pyj.myphoto.view.ui.daily.DailyDetail

@Composable
fun AppDrawer(
    dailyData: DailyData? = null,
    currentRoute: String,
    navigateToPx: () -> Unit,
    navigateToYou: () -> Unit,
    navigateToSetting: () -> Unit,
    closeDrawer: () -> Unit,
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContentColor = MaterialTheme.colorScheme.onBackground,
        //drawerTonalElevation = 10.dp,
        windowInsets = WindowInsets(
            left = 0.dp,
            top = 0.dp,
            right = 0.dp,
            bottom = 0.dp
        )
    ) {
        Top(
            dailyData = dailyData,
            modifier = Modifier.padding(horizontal = 30.dp, vertical = 10.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.px)) },
            icon = { Icon(Icons.Filled.Pets, null) },
            selected = currentRoute == PhotoDestinations.PX_ROUTE,
            onClick = { navigateToPx(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.height(10.dp))

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.you)) },
            icon = { Icon(Icons.Filled.Accessibility, null) },
            selected = currentRoute == PhotoDestinations.YOU_ROUTE,
            onClick = { navigateToYou(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )

        Spacer(modifier = Modifier.height(10.dp))

        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.setting)) },
            icon = { Icon(Icons.Rounded.Settings, null) },
            selected = currentRoute == PhotoDestinations.SETTING_ROUTE,
            onClick = { navigateToSetting(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
private fun Top(dailyData: DailyData? = null, modifier: Modifier = Modifier) {
    if (dailyData != null) {
        DailyDetail(dailyData, modifier)
    }
}
