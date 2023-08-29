package com.pyj.myphoto

import android.content.Context
import com.pyj.myphoto.data.app.AppImpl
import com.pyj.myphoto.data.app.AppRepository
import com.pyj.myphoto.data.px.PxImpl
import com.pyj.myphoto.data.px.PxRepository

interface AppContainer {
    val appRepository: AppRepository
    val pxRepository: PxRepository
}

class AppContainerImpl(private val applicationContext: Context) : AppContainer {

    override val appRepository: AppRepository by lazy {
        AppImpl(applicationContext)
    }

    override val pxRepository: PxRepository by lazy {
        PxImpl()
    }
}
