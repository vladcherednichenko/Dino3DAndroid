package com.vlad.dino3d.objects

import com.vlad.dino3d.Constants
import com.vlad.dino3d.utils.Point3d

abstract class Object3d {

    var positionData: FloatArray = FloatArray(0)
    var colorData: FloatArray = FloatArray(0)
    var normalData: FloatArray = FloatArray(0)

    fun translate(vector: Point3d) {
        for (i in positionData.indices) {

            when (i % Constants.POSITION_COMPONENT_COUNT) {
                0 -> {
                    positionData[i] += vector.x
                }
                1 -> {
                    positionData[i] += vector.y
                }
                2 -> {
                    positionData[i] += vector.z
                }
            }

        }

    }

    fun releaseArrayData() {

        positionData = FloatArray(0)
        normalData = FloatArray(0)
        colorData = FloatArray(0)

    }


}