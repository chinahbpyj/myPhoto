package com.pyj.myphoto.impl

import android.net.Uri

interface PickPhotoInterface {
    fun onSuccess(uri: Uri)

    fun onFail()
}