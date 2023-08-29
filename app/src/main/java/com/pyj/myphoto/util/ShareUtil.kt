package com.pyj.myphoto.util

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.pyj.myphoto.R
import java.util.Calendar

fun sharePost(context: Context, drawable: Drawable) {
    try {
        val bd = drawable as BitmapDrawable
        val bitmap = bd.bitmap
        val uri = Uri.parse(
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                bitmap,
                "IMG" + Calendar.getInstance().time,
                null
            )
        )
        sharePost(context, uri)
    } catch (e: Exception) {
        Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
    }
}

fun sharePost(context: Context, uri: Uri) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.share)
            )
        )
    } catch (e: Exception) {
        Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
    }
}

fun shareText(context: Context, text: String?) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text ?: "")
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.share)
            )
        )
    } catch (e: Exception) {
        Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
    }
}

//分享到指定平台
//text/plain
//application/*
//image/*
//video/*
//audio/*
fun shareToPlatform(context: Context, text: String?) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            `package` = "com.tencent.mm"
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text ?: "")
        }
        context.startActivity(
            Intent.createChooser(
                intent,
                context.getString(R.string.share)
            )
        )
    } catch (e: Exception) {
        Toast.makeText(context, "分享出错了", Toast.LENGTH_SHORT).show()
    }
}




