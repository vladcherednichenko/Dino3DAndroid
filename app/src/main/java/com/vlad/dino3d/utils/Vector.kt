package com.vlad.dino3d.utils

class Vector {
    var x: Float
    var y: Float
    var z: Float

    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    constructor(a: Point3d, b: Point3d) {
        x = b.x - a.x
        y = b.y - a.y
        z = b.z - a.z
    }

    fun invertedVector(): Vector {
        return Vector(-x, -y, -z)
    }

    fun length(): Float {
        return Math.sqrt(
            x * x + y * y + (z * z).toDouble()
        ).toFloat()
    }

    fun crossProduct(other: Vector): Vector {
        return Vector(
            y * other.z - z * other.y,
            z * other.x - x * other.z,
            x * other.y - y * other.x
        )
    }
}