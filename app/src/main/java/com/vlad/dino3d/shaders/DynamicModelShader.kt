package com.vlad.dino3d.shaders

import android.content.Context
import android.opengl.GLES20
import android.opengl.Matrix
import com.vlad.dino3d.R
import com.vlad.dino3d.utils.ShaderHelper
import com.vlad.dino3d.utils.TextResourceReader

class DynamicModelShader(context: Context) : ModelShader(context) {
    // Uniform locations
    private val uMVMatrixLocation: Int
    private val uMVPMatrixLocation: Int
    private val uLightPositionLocation: Int
    fun setUniforms(
        modelMatrix: FloatArray?,
        viewMatrix: FloatArray?,
        projectionMatrix: FloatArray?,
        frontLightPositionInEyeSpace: FloatArray
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
            uLightPositionLocation,
            frontLightPositionInEyeSpace[0],
            frontLightPositionInEyeSpace[1],
            frontLightPositionInEyeSpace[2]
        )
    }

    init {
        // Compile the shaders and link the program.
        program = ShaderHelper.buildProgram(
            TextResourceReader.readTextFileFromResource(
                context,
                R.raw.figure_dynamic_vertex_shader
            ),
            TextResourceReader.readTextFileFromResource(context, R.raw.figure_fragment_shader)
        )
        uMVMatrixLocation = GLES20.glGetUniformLocation(program, U_MV_MATRIX)
        uMVPMatrixLocation = GLES20.glGetUniformLocation(program, U_MVP_MATRIX)
        uLightPositionLocation = GLES20.glGetUniformLocation(program, U_LIGHT_LOCATION)
        uScatterPositionLocation = GLES20.glGetUniformLocation(program, U_SCATTER_VEC)
        uScalePositionLocation = GLES20.glGetUniformLocation(program, U_SCALE_FACTOR)
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION)
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR)
        aNormalLocation = GLES20.glGetAttribLocation(program, A_NORMAL)
    }
}