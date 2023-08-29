package com.pyj.myphoto.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.pyj.myphoto.view.ui.share.ShareCardView

class ImageUtils {
    companion object {
        fun generateShareImage(view: View?): Bitmap? {
            if (view == null) return null

            val bitmap = Bitmap.createBitmap(
                view.width,
                view.height,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            view.layout(
                view.left,
                view.top,
                view.right,
                view.bottom
            )
            view.draw(canvas)
            return bitmap
        }

        @Composable
        fun ComposeUIToView(
            content: @Composable () -> Unit,
            onLoading: () -> Unit,
            onSuccess: (ShareCardView) -> Unit,
        ) {
            onLoading()

            AndroidView(modifier = Modifier.wrapContentSize(),
                factory = {
                    ShareCardView(
                        it,
                        content = {
                            content()
                        }
                    )
                },
                update = {
                    onSuccess(it)
                }
            )
        }
    }
}