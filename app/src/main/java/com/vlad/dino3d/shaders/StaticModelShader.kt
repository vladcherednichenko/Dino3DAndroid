package com.vlad.dino3d.shaders

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.vlad.dino3d.R
import com.vlad.dino3d.utils.ShaderHelper
import com.vlad.dino3d.utils.TextResourceReader

class StaticModelShader(context: Context?) : ModelShader(context) {
    // Uniform locations
    private val uMVMatrixLocation: Int
    private val uMVPMatrixLocation: Int
    private val uFrontLightPositionLocation: Int
    private val uBackLightPositionLocation: Int
    private val uLeftLightPositionLocation: Int
    private val uRightLightPositionLocation: Int
    private val uTopLightPositionLocation: Int
    private val uBottomLightPositionLocation: Int
    var positionAttributeLocation: Int = 0
        get() = aPositionLocation
        set(positionAttributeLocation) {
            field = positionAttributeLocation
        }

    var colorAttributeLocation: Int = 0
        get() = aColorLocation
        set(colorAttributeLocation) {
            field = colorAttributeLocation
        }

    var normalAttributeLocation: Int = 0
        get() = aNormalLocation
        set(normalAttributeLocation) {
            field = normalAttributeLocation
        }

    fun setUniforms(
        modelMatrix: FloatArray?,
        viewMatrix: FloatArray?,
        projectionMatrix: FloatArray?,
        frontLightPositionInEyeSpace: FloatArray,
        backLightPositionInEyeSpace: FloatArray,
        leftLightPositionInEyeSpace: FloatArray,
        rightLightPositionInEyeSpace: FloatArray,
        topLightPositionInEyeSpace: FloatArray,
        bottomLightPositionInEyeSpace: FloatArray
    ) {
        val MVPMatrix = FloatArray(16)
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
// (which currently contains model * view).
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(uMVMatrixLocation, 1, false, MVPMatrix, 0)
        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
// (which now contains model * view * projection).
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0)
        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, MVPMatrix, 0)
        // Pass in the light position in eye space.
        GLES20.glUniform3f(
            uFrontLightPositionLocation,
            frontLightPositionInEyeSpace[0],
            frontLightPositionInEyeSpace[1],
            frontLightPositionInEyeSpace[2]
        )
        GLES20.glUniform3f(
            uBackLightPositionLocation,
            backLightPositionInEyeSpace[0],
            backLightPositionInEyeSpace[1],
            backLightPositionInEyeSpace[2]
        )
        GLES20.glUniform3f(
            uLeftLightPositionLocation,
            leftLightPositionInEyeSpace[0],
            leftLightPositionInEyeSpace[1],
            leftLightPositionInEyeSpace[2]
        )
        GLES20.glUniform3f(
            uRightLightPositionLocation,
            rightLightPositionInEyeSpace[0],
            rightLightPositionInEyeSpace[1],
            rightLightPositionInEyeSpace[2]
        )
        GLES20.glUniform3f(
            uTopLightPositionLocation,
            topLightPositionInEyeSpace[0],
            topLightPositionInEyeSpace[1],
            topLightPositionInEyeSpace[2]
        )
        GLES20.glUniform3f(
            uBottomLightPositionLocation,
            bottomLightPositionInEyeSpace[0],
            bottomLightPositionInEyeSpace[1],
            bottomLightPositionInEyeSpace[2]
        )
    }

    init {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(
                context!!,
                R.raw.figure_static_vertex_shader
            ),
            TextResourceReader.readTextFileFromResource(context, R.raw.figure_fragment_shader)
        )
        uMVMatrixLocation = GLES20.glGetUniformLocation(program, U_MV_MATRIX)
        uMVPMatrixLocation = GLES20.glGetUniformLocation(program, U_MVP_MATRIX)
        uFrontLightPositionLocation = GLES20.glGetUniformLocation(program, U_FRONT_LIGHT_LOCATION)
        uBackLightPositionLocation = GLES20.glGetUniformLocation(program, U_BACK_LIGHT_LOCATION)
        uLeftLightPositionLocation = GLES20.glGetUniformLocation(program, U_LEFT_LIGHT_LOCATION)
        uRightLightPositionLocation = GLES20.glGetUniformLocation(program, U_RIGHT_LIGHT_LOCATION)
        uTopLightPositionLocation = GLES20.glGetUniformLocation(program, U_TOP_LIGHT_LOCATION)
        uBottomLightPositionLocation = GLES20.glGetUniformLocation(program, U_BOTTOM_LIGHT_LOCATION)
        uScatterPositionLocation = GLES20.glGetUniformLocation(program, U_SCATTER_VEC)
        uScalePositionLocation = GLES20.glGetUniformLocation(program, U_SCALE_FACTOR)
        uAlphaPositionLocation = GLES20.glGetUniformLocation(program, U_ALPHA)
        uRedPositionLocation = GLES20.glGetUniformLocation(program, U_RED)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aNormalLocation = GLES20.glGetAttribLocation(program, A_NORMAL)
    }
}