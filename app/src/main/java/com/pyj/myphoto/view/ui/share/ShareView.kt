package com.pyj.myphoto.view.ui.share

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AbstractComposeView

@SuppressLint("ViewConstructor")
class ShareCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    val content: @Composable () -> Unit
) : AbstractComposeView(context, attrs, defStyleAttr) {

    @Composable
    override fun Content() {
        content()
    }
}

