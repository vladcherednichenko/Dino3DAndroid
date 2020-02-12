package com.vlad.dino3d.utils
import android.content.Context
import java.util.*

object CubeDataHolder {

    private const val POINT_COMPONENT_COUNT = 3
    private const val FACET_COMPONENT_COUNT = 3

    var sizeInVertex = 0
    var backgroundColor = "#ffffff"
    lateinit var model: IntArray
    var sizeX = 0
    var sizeZ = 0
    val sizeY
        get() = model.size / (sizeX * sizeZ)
    var cubeNumber = 0

    var facetListLow: ArrayList<Facet> = arrayListOf()
    var facetListMedium: ArrayList<Facet> = arrayListOf()
    var facetListHigh: ArrayList<Facet> = arrayListOf()
    lateinit var vertices: FloatArray
    lateinit var normals: FloatArray
    var facetList: ArrayList<Facet>? = null
        private set

    fun setFacetList(facetList: ArrayList<Facet>) {
        this.facetList = facetList
        vertices =
            FloatArray(facetList.size * FACET_COMPONENT_COUNT * POINT_COMPONENT_COUNT)
        normals =
            FloatArray(facetList.size * FACET_COMPONENT_COUNT * POINT_COMPONENT_COUNT)
        var verticesPointer = 0
        var normalsPointer = 0
        for (facet in facetList) {
            normals[normalsPointer++] = facet.normal.x
            normals[normalsPointer++] = facet.normal.y
            normals[normalsPointer++] = facet.normal.z
            normals[normalsPointer++] = facet.normal.x
            normals[normalsPointer++] = facet.normal.y
            normals[normalsPointer++] = facet.normal.z
            normals[normalsPointer++] = facet.normal.x
            normals[normalsPointer++] = facet.normal.y
            normals[normalsPointer++] = facet.normal.z
            vertices[verticesPointer++] = facet.A.x
            vertices[verticesPointer++] = facet.A.y
            vertices[verticesPointer++] = facet.A.z
            vertices[verticesPointer++] = facet.B.x
            vertices[verticesPointer++] = facet.B.y
            vertices[verticesPointer++] = facet.B.z
            vertices[verticesPointer++] = facet.C.x
            vertices[verticesPointer++] = facet.C.y
            vertices[verticesPointer++] = facet.C.z
        }

        sizeInVertex = vertices.size / 3

    }



    fun loadFacetList(context: Context) {
        facetListMedium = TextResourceReader.getFacetsFromFileObject(context, "cube_medium.obj")
    }

    fun setGraphicsQuality(quality: Int) {
        when (quality) {
            0 -> {
                setFacetList(facetListHigh)
            }
            1 -> {
                setFacetList(facetListMedium)
            }
            2 -> {
                setFacetList(facetListHigh)
            }
        }
    }

}