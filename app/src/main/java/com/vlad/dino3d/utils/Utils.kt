package com.vlad.dino3d.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService

object Utils {

    fun hideDefaultControls(activity: Activity) {
        val window = activity.window ?: return
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        val decorView = window.decorView
        if (decorView != null) {
            var uiOptions = decorView.systemUiVisibility
            if (Build.VERSION.SDK_INT >= 14) {
                uiOptions = uiOptions or View.SYSTEM_UI_FLAG_LOW_PROFILE
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOptions = uiOptions or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions = uiOptions or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
            decorView.systemUiVisibility = uiOptions
        }
    }

    fun showDefaultControls(activity: Activity) {
        val window = activity.window ?: return
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        val decorView = window.decorView
        if (decorView != null) {
            var uiOptions = decorView.systemUiVisibility
            if (Build.VERSION.SDK_INT >= 14) {
                uiOptions = uiOptions and View.SYSTEM_UI_FLAG_LOW_PROFILE.inv()
            }
            if (Build.VERSION.SDK_INT >= 16) {
                uiOptions = uiOptions and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION.inv()
            }
            if (Build.VERSION.SDK_INT >= 19) {
                uiOptions = uiOptions and View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY.inv()
            }
            decorView.systemUiVisibility = uiOptions
        }
    }


}