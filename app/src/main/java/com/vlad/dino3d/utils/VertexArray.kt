package com.vlad.dino3d.utils

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class VertexArray(vertexData: FloatArray) {

    private val BYTES_PER_FLOAT = 4

    private var floatBuffer: FloatBuffer?
    // Without VBOs
    fun setVertexAttribPointer(
        dataOffset: Int,
        attributeLocation: Int,
        componentCount: Int,
        stride: Int
    ) {
        floatBuffer!!.position(dataOffset)
        GLES20.glVertexAttribPointer(
            attributeLocation,
            componentCount,
            GLES20.GL_FLOAT,
            false,
            stride,
            floatBuffer
        )
        GLES20.glEnableVertexAttribArray(attributeLocation)
        floatBuffer!!.position(0)
    }

    // With VBOs
    fun bindBufferToVBO(VBOPointer: Int) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, VBOPointer)
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            floatBuffer!!.capacity() * BYTES_PER_FLOAT,
            floatBuffer,
            GLES20.GL_STATIC_DRAW
        )
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        floatBuffer!!.limit(0)
        floatBuffer = null
    }

    init {
        floatBuffer = ByteBuffer
            .allocateDirect(vertexData.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .put(vertexData)
        floatBuffer?.position(0)
    }
}