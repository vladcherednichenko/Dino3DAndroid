package com.vlad.dino3d

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MotionEvent
import androidx.core.graphics.rotationMatrix
import com.vlad.dino3d.min3d.Utils.DEG
import com.vlad.dino3d.min3d.core.Object3dContainer
import com.vlad.dino3d.min3d.core.RendererActivity
import com.vlad.dino3d.min3d.objectPrimitives.Box
import com.vlad.dino3d.min3d.parser.Parser
import com.vlad.dino3d.min3d.vos.Light
import com.vlad.dino3d.min3d.vos.LightType
import com.vlad.dino3d.utils.Utils
import java.lang.Math.pow
import kotlin.math.*


class Game3DActivity : RendererActivity() {

    private var previousTouchCenterX = 0f
    private var previousTouchCenterY = 0f


    private var startingDistanceBetweenPointers = 0f
    private val movingSensitivity = 15f // from 0 to 100

    private var mPreviousX = 0f
    private var mPreviousY = 0f


    private val faceObject3D: Object3dContainer? = null
    private val _o1: Object3dContainer? = null
    private val _k: Object3dContainer? = null
    private var _dx = 0f
    private var _dy = 0f
    private var camRotationX = 0f
    private var camRotationY = 0f
    private var objModel: Object3dContainer? = null
    private var light: Light? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        Utils.hideDefaultControls(this)
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun initScene() {

        val parser = Parser.createParser(
            Parser.Type.OBJ,
            resources, "com.vlad.dino3d:raw/dino1_obj", true
        )
        parser.parse()
        objModel = parser.parsedObject

        objModel?.scale()?.z = .3f
        objModel?.scale()?.y = objModel?.scale()?.z
        objModel?.scale()?.x = objModel?.scale()?.y


        objModel?.position()!!.y -= 1f
        //objModel?.rotation()!!.y += 90f


        light = Light()
        light!!.ambient.setAll(-0x100)
        light!!.diffuse.setAll(-0x100)
        light!!.type(LightType.POSITIONAL)

        var box = Box (1.0f, 1.0f, 1.0f)

        scene.lights().add(light)
        scene.addChild(box)
        //scene.addChild(objModel)

        //scene.camera().target = objModel!!.position()

    }

    override fun updateScene() {



    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if(Settings.enabledCamera) updateCamera(event)

        return true

    }

    private fun updateCamera(event: MotionEvent){

        val x = event.x
        val y = event.y

        if(event.actionMasked == MotionEvent.ACTION_DOWN){

            mPreviousX = x
            mPreviousY = y

        }

        if(event.actionMasked == MotionEvent.ACTION_MOVE){

            _dx = (x - mPreviousX) * (movingSensitivity / 100f)
            _dy = (y - mPreviousY) * (movingSensitivity / 100f)

            mPreviousX = x
            mPreviousY = y

        }

        if(event.actionMasked == MotionEvent.ACTION_UP){

            _dx = 0f
            _dy = 0f

        }

        camRotationX += _dx
        camRotationY += _dy

        val radius = 5f

        val camY = sin(camRotationY * DEG.toDouble()).toFloat() * radius
        val temp = cos(camRotationY * DEG.toDouble()).toFloat() * radius
        val camX = sin(camRotationX * DEG.toDouble()).toFloat() * temp
        val camZ = cos(camRotationX * DEG.toDouble()).toFloat() * temp

        scene.camera().position.setAll(-camX,camY,camZ)


        _dx = 0f
        _dy = 0f

    }
    


}