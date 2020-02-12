package com.vlad.dino3d.objects

import android.opengl.GLES20.*
import com.vlad.dino3d.Constants.COLOR_COORDINATES_COMPONENT_COUNT
import com.vlad.dino3d.Constants.NORMAL_COMPONENT_COUNT
import com.vlad.dino3d.Constants.POSITION_COMPONENT_COUNT
import com.vlad.dino3d.shaders.ModelShader
import com.vlad.dino3d.utils.Vector
import com.vlad.dino3d.utils.VertexArray

class FigureBuilder {

    private var figures: MutableList<Object3d> = mutableListOf()

    private var vertexPosArray: VertexArray? = null
    private var vertexColorArray: VertexArray? = null
    private var vertexNormalArray: VertexArray? = null

    private var vertexPositionData: FloatArray = FloatArray(0)
    private var vertexColorData: FloatArray = FloatArray(0)
    private var vertexNormalData: FloatArray = FloatArray(0)

    private var vertexDataOffset = 0
    private var vertexColorDataOffset = 0
    private var vertexNormalDataOffset = 0

    private var vertexBufferPositionIdx = 0
    private var vertexBufferColorIdx = 0
    private var vertexBufferNormalIdx = 0

    private var vertexNumber = 0

    init {



    }

    fun addObject(obj: Object3d) {

        figures.add(obj)
        build()
        bindAttributesData()

    }

    private fun strideFigure(strideVector: Vector) {

        build()
        bindAttributesData()

    }

    fun build(){

        countVertices()
        vertexColorDataOffset = 0
        vertexDataOffset = 0
        vertexNormalDataOffset = 0

        vertexPositionData = FloatArray(vertexNumber * POSITION_COMPONENT_COUNT)
        vertexNormalData = FloatArray(vertexNumber * NORMAL_COMPONENT_COUNT)
        vertexColorData = FloatArray(vertexNumber * COLOR_COORDINATES_COMPONENT_COUNT)

        figures.forEach {

            appendObject(it)

        }

        vertexPosArray = VertexArray(vertexPositionData)
        vertexColorArray = VertexArray(vertexColorData)
        vertexNormalArray = VertexArray(vertexNormalData)

        // clear unused data
        vertexPositionData = FloatArray(0)
        vertexNormalData = FloatArray(0)
        vertexColorData = FloatArray(0)

    }

    private fun appendObject(obj: Object3d) {

        for (f in obj.positionData) {
            vertexPositionData[vertexDataOffset++] = f
        }
        for (f in obj.normalData) {
            vertexNormalData[vertexNormalDataOffset++] = f
        }
        for (f in obj.colorData) {
            vertexColorData[vertexColorDataOffset++] = f
        }

    }

    fun bindAttributesData() {

        if (vertexNumber <= 0) { return }

        glDeleteBuffers(
            3,
            intArrayOf(
                vertexBufferPositionIdx,
                vertexBufferColorIdx,
                vertexBufferNormalIdx),
            0)

        val buffers = IntArray(3)
        glGenBuffers(3, buffers, 0)
        glBindBuffer(GL_ARRAY_BUFFER, 0)
        vertexBufferPositionIdx = buffers[0]
        vertexBufferColorIdx = buffers[1]
        vertexBufferNormalIdx = buffers[2]
        vertexPosArray!!.bindBufferToVBO(vertexBufferPositionIdx)
        vertexColorArray!!.bindBufferToVBO(vertexBufferColorIdx)
        vertexNormalArray!!.bindBufferToVBO(vertexBufferNormalIdx)

    }

    fun draw(shader : ModelShader){

        if (vertexNumber <= 0) { return }

        //draw figure
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferPositionIdx)
        glEnableVertexAttribArray(shader.aPositionLocation)
        glVertexAttribPointer(shader.aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false,0, 0)


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferColorIdx)
        glEnableVertexAttribArray(shader.aColorLocation)
        glVertexAttribPointer(shader.aColorLocation, COLOR_COORDINATES_COMPONENT_COUNT, GL_FLOAT, false, 0, 0)


        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferNormalIdx)
        glEnableVertexAttribArray(shader.aNormalLocation)
        glVertexAttribPointer(shader.aNormalLocation, NORMAL_COMPONENT_COUNT, GL_FLOAT, false, 0, 0)

        shader.setScatter(floatArrayOf(0.0f, 0.0f, 0.0f))
        glDrawArrays(GL_TRIANGLES, 0, vertexNumber)

    }

    private fun countVertices(){

        vertexNumber = 0

        figures.forEach {

            vertexNumber += it.positionData.size

        }

        vertexNumber /= POSITION_COMPONENT_COUNT

    }



}