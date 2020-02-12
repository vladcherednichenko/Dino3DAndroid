package com.vlad.dino3d.utils

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

object TextResourceReader {
    private const val modelsLocation = "models/"
    fun readTextFileFromResource(
        context: Context,
        resourceId: Int
    ): String {
        val body = StringBuilder()
        try {
            val inputStream =
                context.resources.openRawResource(resourceId)
            val inputStreamReader =
                InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String?
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                body.append(nextLine)
                body.append('\n')
            }
        } catch (e: IOException) {
            throw RuntimeException(
                "Could not open resource: $resourceId", e
            )
        } catch (nfe: NotFoundException) {
            throw RuntimeException("Resource not found: $resourceId", nfe)
        }
        return body.toString()
    }

    fun getFacetsFromModel(
        context: Context,
        resourceId: Int
    ): ArrayList<Facet> {
        val facetList =
            ArrayList<Facet>()
        var facet = Facet()
        try {
            val inputStream =
                context.resources.openRawResource(resourceId)
            val inputStreamReader =
                InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var nextLine: String
            while (bufferedReader.readLine().also { nextLine = it } != null) {
                if (nextLine.contains("normal")) {
                    facet = Facet()
                    val coords =
                        nextLine.substring(nextLine.indexOf("normal")).split("\\s+").toTypedArray()
                    val x = coords[1].toFloat()
                    val y = coords[2].toFloat()
                    val z = coords[3].toFloat()
                    facet.normal = Vector(x, y, z)
                } else if (nextLine.contains("vertex")) {
                    val coords =
                        nextLine.substring(nextLine.indexOf("vertex")).split("\\s+").toTypedArray()
                    val x = coords[1].toFloat()
                    val y = coords[2].toFloat()
                    val z = coords[3].toFloat()
                    if (facet.A == null) {
                        facet.A = Point3d(x, y, z)
                    } else if (facet.B == null) {
                        facet.B = Point3d(x, y, z)
                    } else if (facet.C == null) {
                        facet.C = Point3d(x, y, z)
                        facetList.add(facet)
                    }
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(
                "Could not open resource: $resourceId", e
            )
        } catch (nfe: NotFoundException) {
            throw RuntimeException("Resource not found: $resourceId", nfe)
        }
        return facetList
    }

    fun getFacetsFromFileObject(
        context: Context,
        file: String
    ): ArrayList<Facet> {
        var file = file
        file = modelsLocation + file
        //file = "lalala/shit.txt"
        val facetList =
            ArrayList<Facet>()
        val verticesList = ArrayList<String>()
        val facesList = ArrayList<String>()
        // Open the OBJ file with p Scanner
        var scanner: Scanner? = null
        try {

            scanner = Scanner(context.assets.open(file))
            // Loop through all its lines
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                if (line.startsWith("v ")) { // Add vertex line to list of vertices
                    verticesList.add(line)
                } else if (line.startsWith("f ")) { // Add face line to faces list
                    facesList.add(line)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        for (face in facesList) {
            val pointList = face.split(" ").toTypedArray()
            val verticeAPosition = pointList[1].toShort() - 1
            val verticeBPosition = pointList[2].toShort() - 1
            val verticeCPosition = pointList[3].toShort() - 1
            val verticeAList =
                verticesList[verticeAPosition].split(" ").toTypedArray()
            val verticeBList =
                verticesList[verticeBPosition].split(" ").toTypedArray()
            val verticeCList =
                verticesList[verticeCPosition].split(" ").toTypedArray()
            val A = Point3d(
                verticeAList[1].toFloat(),
                verticeAList[2].toFloat(),
                verticeAList[3].toFloat()
            )
            val B = Point3d(
                verticeBList[1].toFloat(),
                verticeBList[2].toFloat(),
                verticeBList[3].toFloat()
            )
            val C = Point3d(
                verticeCList[1].toFloat(),
                verticeCList[2].toFloat(),
                verticeCList[3].toFloat()
            )
            val facet =
                Facet(calcNormals(A, B, C), A, B, C)
            facetList.add(facet)
        }
        // Close the scanner
        scanner?.close()
        return facetList
    }

    fun calcNormals(a: Point3d, b: Point3d, c: Point3d): Vector {
        val result = Vector(0f, 0f, 0f)
        val wrki: Double
        val v1 = Vector(
            a.x - b.x,
            a.y - b.y,
            a.z - b.z
        )
        val v2 = Vector(
            b.x - c.x,
            b.y - c.y,
            b.z - c.z
        )
        wrki = Math.sqrt(
            Math.pow(v1.y * v2.z - v1.z * v2.y.toDouble(), 2.0) +
                    Math.pow(v1.z * v2.x - v1.x * v2.z.toDouble(), 2.0) +
                    Math.pow(v1.x * v2.y - v1.y * v2.x.toDouble(), 2.0)
        )
        result.x = ((v1.y * v2.z - v1.z * v2.y) / wrki).toFloat()
        result.y = ((v1.z * v2.x - v1.x * v2.z) / wrki).toFloat()
        result.z = ((v1.x * v2.y - v1.y * v2.x) / wrki).toFloat()
        return result
    }
}