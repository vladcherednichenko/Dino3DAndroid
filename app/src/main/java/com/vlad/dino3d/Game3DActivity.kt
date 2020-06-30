package com.vlad.dino3d

import android.os.Bundle
import android.os.PersistableBundle
import android.view.MotionEvent
import com.vlad.dino3d.min3d.Utils.DEG
import com.vlad.dino3d.min3d.animation.AnimationObject3d
import com.vlad.dino3d.min3d.core.Object3dContainer
import com.vlad.dino3d.min3d.core.RendererActivity
import com.vlad.dino3d.min3d.objectPrimitives.Box
import com.vlad.dino3d.min3d.parser.Parser
import com.vlad.dino3d.min3d.vos.Light
import com.vlad.dino3d.min3d.vos.LightType
import com.vlad.dino3d.min3d.vos.ShadeModel
import com.vlad.dino3d.utils.Utils
import kotlin.math.cos
import kotlin.math.sin


class Game3DActivity : RendererActivity() {

    private val movingSensitivity = 15f // from 0 to 100

    private var mPreviousX = 0f
    private var mPreviousY = 0f


    private val faceObject3D: Object3dContainer? = null
    private var _dx = 0f
    private var _dy = 0f

    private var camRotationX = 0f
    private var camRotationY = 0f

    private var cameraDistance = 10f

    private var dino = Object3dContainer()
    private var light = Light()

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        Utils.hideDefaultControls(this)
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun initScene() {

        scene.backgroundColor().setAll(204, 204, 204, 255)

        val parser = Parser.createParser(
            Parser.Type.OBJ,
            resources, "com.vlad.dino3d:raw/dino1_obj", true
        )
        parser.parse()


        dino = parser.parsedObject

        dino.scale()?.z = .6f
        dino.scale()?.y = dino?.scale()?.z
        dino.scale()?.x = dino?.scale()?.y


        dino.position()!!.y -= 1f
        //objModel?.rotation()!!.y += 90f


        light.position.setAll(0f, 0f, 10f)
        light.type(LightType.POSITIONAL)
        //var shade =  ShadeModel()
        var animatedObject3dContainer = AnimationObject3d()

        var box = Box (12.0f, 0.1f, 1.0f)

        scene.lights().add(light)
        scene.addChild(box)
        scene.camera().position.x = 0f
        scene.camera().position.y = 0f
        scene.camera().position.z = cameraDistance
        box.addChild(dino)
        dino.position().z += 0.5f
        dino.position().y += 0.5f


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

        when(event.actionMasked){

            MotionEvent.ACTION_DOWN -> {
                mPreviousX = x
                mPreviousY = y
            }
            MotionEvent.ACTION_MOVE -> {

                _dx = (x - mPreviousX) * (movingSensitivity / 100f)
                _dy = (y - mPreviousY) * (movingSensitivity / 100f)

                mPreviousX = x
                mPreviousY = y

            }
            MotionEvent.ACTION_UP -> {

                _dx = 0f
                _dy = 0f

            }

        }

        camRotationX += _dx
        camRotationY += _dy

        val camY = sin(camRotationY * DEG.toDouble()).toFloat() * cameraDistance
        val temp = cos(camRotationY * DEG.toDouble()).toFloat() * cameraDistance
        val camX = sin(camRotationX * DEG.toDouble()).toFloat() * temp
        val camZ = cos(camRotationX * DEG.toDouble()).toFloat() * temp

        scene.camera().position.setAll(-camX,camY,camZ)

        _dx = 0f
        _dy = 0f

    }



}