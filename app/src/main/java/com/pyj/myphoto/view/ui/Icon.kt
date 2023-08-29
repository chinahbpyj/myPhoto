package com.pyj.myphoto.view.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbUpOffAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics

@Composable
fun DownLoadButton(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Download,
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun WallpaperButton(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Bolt,
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.ThumbUpOffAlt,
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    isBookmarked: Boolean,
    onClick: () -> Unit
) {
    IconToggleButton(
        checked = isBookmarked,
        onCheckedChange = { onClick() },
        modifier = modifier.semantics {
            this.onClick(label = "", action = null)
        }
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
            contentDescription = null,
            tint = tint
        )
    }
}

@Composable
fun ShareButton(
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            imageVector = Icons.Filled.Share,
            contentDescription = null,
            tint = tint
        )
    }
}