package com.vlad.dino3d.shaders

import android.content.Context
import android.opengl.GLES20

abstract class ModelShader(context: Context?) {
    // Uniform constants
    protected val U_MVP_MATRIX = "u_MVPMatrix"
    protected val U_MV_MATRIX = "u_MVMatrix"

    protected val U_FRONT_LIGHT_LOCATION = "u_FrontLightPos"
    protected val U_BACK_LIGHT_LOCATION = "u_BackLightPos"
    protected val U_LEFT_LIGHT_LOCATION = "u_LeftLightPos"
    protected val U_RIGHT_LIGHT_LOCATION = "u_RightLightPos"
    protected val U_TOP_LIGHT_LOCATION = "u_TopLightPos"
    protected val U_BOTTOM_LIGHT_LOCATION = "u_BottomLightPos"

    protected var uScatterPositionLocation = 0
    protected var uScalePositionLocation = 0
    protected var uAlphaPositionLocation = 0
    protected var uRedPositionLocation = 0
    //dynamic shader
    protected val U_LIGHT_LOCATION = "u_LightPos"

    protected val U_SCATTER_VEC = "u_ScatterVec"
    protected val U_SCALE_FACTOR = "u_ScaleFactor"
    protected val U_ALPHA = "u_Alpha"
    protected val U_RED = "u_Red"

    // Attribute constants
    protected val A_POSITION = "a_Position"
    protected val A_COLOR = "a_Color"
    protected val A_NORMAL = "a_Normal"


    // Attribute locations
    var aPositionLocation = 0
    var aColorLocation = 0
    var aNormalLocation = 0

    protected var program = 0


    open fun ModelShader(context: Context?) {}

    open fun setScatter(scatterVector: FloatArray) {
        GLES20.glUniform3f(
            uScatterPositionLocation,
            scatterVector[0],
            scatterVector[1],
            scatterVector[2]
        )
    }

    open fun setRed(red: Float) {
        GLES20.glUniform1f(uRedPositionLocation, red)
    }

    open fun setAlpha(alpha: Float) {
        GLES20.glUniform1f(uAlphaPositionLocation, alpha)
    }

    open fun setScaleFactor(scaleFactor: Float) {
        GLES20.glUniform1f(uScalePositionLocation, scaleFactor)
    }

    open fun useProgram() { // Set the current OpenGL shader program to this program.
        GLES20.glUseProgram(program)
    }
}