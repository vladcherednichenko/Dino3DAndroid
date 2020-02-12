package com.vlad.dino3d.utils

class Point3d(var x: Float, var y: Float, var z: Float) {
    fun translateX(dx: Float) {
        x += dx
    }

    fun translateY(dy: Float) {
        y += dy
    }

    fun translateZ(dz: Float) {
        z += dz
    }

    fun translate(vector: Vector) {
        x += vector.x
        y += vector.y
        z += vector.z
    }

    fun translateAndCopy(vector: Vector): Point3d {
        return Point3d(
            x + vector.x,
            y + vector.y,
            z + vector.z
        )
    }

    fun clone(): Point3d {
        return Point3d(x, y, z)
    }

    fun equals(secondPoint: Point3d): Boolean {
        return x == secondPoint.x && y == secondPoint.y && z == secondPoint.z
    }

    fun pointBetween(pointOne: Point3d, pointTwo: Point3d): Point3d {
        return Point3d(
            (pointOne.x + pointTwo.x) / 2,
            (pointOne.y + pointTwo.y) / 2,
            (pointOne.z + pointTwo.z) / 2
        )
    }

}
