package com.pyj.myphoto.util

import android.os.Build

/**
 * 判断是否为华为系统
 */
val isHuaweiRom: Boolean
    get() {
        val brand = Build.BRAND
        if (brand.lowercase().contains("huawei")) {
            return true
        }
        return false
    }

/**
 * 判断是否为小米系统
 */
val isMiuiRom: Boolean
    get() {
        val brand = Build.BRAND
        if (brand.lowercase().contains("xiaomi") || brand.lowercase().contains("redmi")) {
            return true
        }
        return false
    }

/**
 * 判断是否是vivo系统
 */
val isVivoRom: Boolean
    get() {
        val brand = Build.BRAND
        if (brand.lowercase().contains("vivo")) {
            return true
        }
        return false
    }

/**
 * 判断是否是oppo系统
 */
val isOppoRom: Boolean
    get() {
        val brand = Build.BRAND
        if (brand.lowercase().contains("oppo")) {
            return true
        }
        return false
    }

/**
 * 判断是否是一加系统
 */
val isOnePlusRom: Boolean
    get() {
        val brand = Build.BRAND
        if (brand.lowercase().contains("oneplus")) {
            return true
        }
        return false
    }