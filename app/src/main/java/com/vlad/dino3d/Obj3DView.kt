package com.vlad.dino3d

import android.view.MotionEvent
import com.vlad.dino3d.min3d.Utils.DEG
import com.vlad.dino3d.min3d.core.Object3dContainer
import com.vlad.dino3d.min3d.core.RendererActivity
import com.vlad.dino3d.min3d.objectPrimitives.Box
import com.vlad.dino3d.min3d.parser.Parser
import com.vlad.dino3d.min3d.vos.Light
import com.vlad.dino3d.min3d.vos.LightType
import kotlin.math.cos
import kotlin.math.sin


class Obj3DView : RendererActivity() {

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
    private var _rotX = 0f
    private var _rotY = 0f
    private var objModel: Object3dContainer? = null
    private var light: Light? = null
    override fun initScene() {


        val parser = Parser.createParser(
            Parser.Type.OBJ,
            resources, "com.vlad.dino3d:raw/dino1_obj", true
        )
        parser.parse()
        objModel = parser.parsedObject

        objModel?.scale()?.z = .5f
        objModel?.scale()?.y = objModel?.scale()?.z
        objModel?.scale()?.x = objModel?.scale()?.y

        objModel?.position()!!.y -= 15f
        objModel?.rotation()!!.y += 90f


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

        _rotX += _dx
        _rotY += _dy

        val x: Float = sin(_rotX * DEG.toDouble()).toFloat() * 5f
        val z: Float = cos(_rotX * DEG.toDouble()).toFloat() * 5f
        val y: Float = sin(_rotY * DEG.toDouble()).toFloat() * 5f

        scene.camera().position.setAll(-x,y,z)

        //scene.camera().position.rotateX(_dx)
        //scene.camera().position.rotateY( -_dx)

        _dx = 0f
        _dy = 0f

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

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

        return true

    }

    private fun resetPointersValues() {
        startingDistanceBetweenPointers = 0f
        previousTouchCenterX = 0f
        previousTouchCenterY = 0f
    }
}