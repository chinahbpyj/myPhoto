package com.pyj.myphoto.data.app

interface AppRepository {
    suspend fun userData(default: Boolean): UserData
}