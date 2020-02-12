package com.vlad.dino3d.objects

import com.vlad.dino3d.utils.ColorRGB
import com.vlad.dino3d.utils.CubeDataHolder
import com.vlad.dino3d.utils.Point3d

class Cube(var center: Point3d, var color: ColorRGB) : Comparable<Cube>, Object3d() {

    private val TAG = "Cube"

    constructor(center: Point3d, color: ColorRGB, autoCreateVertexData: Boolean) : this(center, color) {

        if (autoCreateVertexData) {
            createCubeData()
        }

    }

    fun createCubeData() {

        positionData = CubeDataHolder.vertices.clone()
        normalData = CubeDataHolder.normals.clone()
        colorData = FloatArray(normalData.size + normalData.size / 3)

        for (i in colorData.indices) {
            when (i % 4) {
                0 -> {
                    colorData[i] = color.RED
                }
                1 -> {
                    colorData[i] = color.GREEN
                }
                2 -> {
                    colorData[i] = color.BLUE
                }
                3 -> {
                    colorData[i] = color.ALPHA
                }
            }
        }

        translate(center)

    }


    override fun compareTo(other: Cube): Int {
        val compareOrder = other.center.y

        return Math.round(this.center.y - compareOrder)
    }

    fun equals (other: Cube): Boolean{

        return center.equals(other.center)

    }

}