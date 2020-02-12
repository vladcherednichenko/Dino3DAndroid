package com.vlad.dino3d

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vlad.dino3d.objects.Cube
import com.vlad.dino3d.utils.CubeDataHolder
import com.vlad.dino3d.utils.Utils


class RenderActivity : AppCompatActivity() {

    private lateinit var gameSurface : GLSurfaceView
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        super.onCreate(savedInstanceState)
        CubeDataHolder.loadFacetList(applicationContext)
        CubeDataHolder.setGraphicsQuality(1)
        setContentView(R.layout.activity_main)

        if(!checkIfSupportsGLES3()){ return }
        else rendererSet = true

    }



    private fun checkIfSupportsGLES3(): Boolean{

        val activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000
        if(!supportsEs3) showToast("openGL 3.0 is not supported")
        return supportsEs3

    }

    fun showToast(text: String) {

        runOnUiThread {

            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()

        }

    }

    override fun onResume() {
        super.onResume()

        Utils.hideDefaultControls(this)

    }



}
