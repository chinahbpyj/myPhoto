package com.pyj.myphoto.util

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.pyj.myphoto.view.ui.dialog.StoragePermissionDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun StoragePermission(
    title1: String = "设置壁纸，请先授权",
    title2: String = "请授权",
    text: String = "存储权限用于保存图片去设置壁纸",
    granted: () -> Unit,
    onPermissionResult: (Boolean) -> Unit = {}
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        granted()
        return
    }

    var launchPermission by remember { mutableStateOf(false) }

    // Storage permission state
    val storagePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        onPermissionResult = onPermissionResult
    )

    when (storagePermissionState.status) {
        // If the camera permission is granted, then show screen with the feature enabled
        PermissionStatus.Granted -> {
            granted()
        }

        is PermissionStatus.Denied -> {
            launchPermission = true
        }
    }

    if (launchPermission) {
        val title =
            if ((storagePermissionState.status as PermissionStatus.Denied).shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                title1
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                title2
            }

        StoragePermissionDialog(
            title = title,
            text = text,
            onConfirm = {
                launchPermission = false
                storagePermissionState.launchPermissionRequest()
            },
            onDismiss = { launchPermission = false }
        )
    }
}