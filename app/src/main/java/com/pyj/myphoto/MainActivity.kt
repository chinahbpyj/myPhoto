package com.pyj.myphoto

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.pyj.myphoto.data.DailyData
import com.pyj.myphoto.impl.PickPhotoInterface
import com.pyj.myphoto.viewmodel.app.AppUiState
import com.pyj.myphoto.viewmodel.app.AppViewModel

class MainActivity : ComponentActivity() {
    lateinit var globalUiState: AppUiState
    private var pickPhotoInterface: PickPhotoInterface? = null

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    pickPhotoInterface?.onSuccess(uri)
                } else {
                    pickPhotoInterface?.onFail()
                }
            }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition {
            false
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val appContainer = (application as MyApplication).container
        setContent {
            val context = LocalContext.current

            val appViewModel: AppViewModel = viewModel(
                factory = AppViewModel.provideFactory(context)
            )

            val appUiState by appViewModel.uiState.collectAsStateWithLifecycle()
            globalUiState = appUiState

            val systemUiController = rememberSystemUiController()

            val isSystemInDarkTheme: Boolean = isSystemInDarkTheme()

            appViewModel.initUserData(appContainer.appRepository, isSystemInDarkTheme)

            val darkTheme = appUiState.userData?.darkTheme ?: false

            NetworkStatus(context, appViewModel, appUiState.networkConnected, appUiState.dailyData)

            // Update the dark content of the system bars to match the theme
            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme

                onDispose {}
            }

            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass
            PhotoApp(
                appViewModel = appViewModel,
                appUiState = appUiState,
                enabledSwitch = !isSystemInDarkTheme,
                darkTheme = darkTheme,
                appContainer,
                widthSizeClass,
                pickPhoto = {
                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }
            )
        }
    }

    fun setPickPhotoInterface(pickPhotoInterface: PickPhotoInterface) {
        this.pickPhotoInterface = pickPhotoInterface
    }
}

@Composable
private fun NetworkStatus(
    context: Context,
    appViewModel: AppViewModel,
    networkConnected: Boolean,
    dailyData: DailyData?
) {
    DisposableEffect(context, networkConnected, dailyData) {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val connectivityManager =
                    context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                val isConnected = networkInfo != null && networkInfo.isConnected

                if (isConnected && !networkConnected && dailyData == null) {
                    appViewModel.daily()
                }

                appViewModel.networkConnectChanged(isConnected)
            }
        }
        context.registerReceiver(broadcastReceiver, intentFilter)
        onDispose {
            context.unregisterReceiver(broadcastReceiver)
        }
    }
}

