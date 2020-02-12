package com.vlad.dino3d.objects

import java.nio.FloatBuffer

class Table {

    var tableVertices = floatArrayOf(

        // Triangle 1
        0f, 0f,
        9f, 14f,
        0f, 14f,
        // Triangle 2
        0f, 0f,
        9f, 0f,
        9f, 14f,

        // Line 1
        0f, 7f,
        9f, 7f,
        // Mallets
        4.5f, 2f,
        4.5f, 12f

    )

    private val BYTES_PER_FLOAT = 4
    private var vertexData: FloatBuffer? = null





}