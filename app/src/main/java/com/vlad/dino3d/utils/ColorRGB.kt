package com.vlad.dino3d.utils

class ColorRGB {
    var RED = 0f
    var GREEN = 0f
    var BLUE = 0f
    var ALPHA = 1.0f
    var hexColor: String? = null

    constructor(hexColor: String) {
        this.hexColor = hexColor
        RED = Integer.valueOf(hexColor.substring(1, 3), 16).toFloat() / 255
        GREEN = Integer.valueOf(hexColor.substring(3, 5), 16).toFloat() / 255
        BLUE = Integer.valueOf(hexColor.substring(5, 7), 16).toFloat() / 255
    }

    constructor(hexColor: String, alpha: Float) : this(hexColor) {
        setAlpha(alpha)
    }

    constructor(r: Int, g: Int, b: Int) {
        RED = r.toFloat() / 255f
        GREEN = g.toFloat() / 255f
        BLUE = b.toFloat() / 255f
    }

    constructor(r: Int, g: Int, b: Int, alpha: Float) {
        RED = r.toFloat() / 255f
        GREEN = g.toFloat() / 255f
        BLUE = b.toFloat() / 255f
        setAlpha(alpha)
    }

    private fun setAlpha(alpha: Float) {
        ALPHA = if (alpha > 1.0f || alpha < 0.0f) 1.0f else alpha
    }

    companion object {
        val default: ColorRGB
            get() = ColorRGB(255, 255, 255)
    }
}