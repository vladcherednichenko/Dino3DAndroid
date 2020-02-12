package com.vlad.dino3d

import android.opengl.GLES20
import android.opengl.GLES20.GL_COLOR_BUFFER_BIT
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.vlad.dino3d.objects.Cube
import com.vlad.dino3d.objects.FigureBuilder
import com.vlad.dino3d.shaders.DynamicModelShader
import com.vlad.dino3d.utils.ColorRGB
import com.vlad.dino3d.utils.Point3d
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GameRenderer (val surfaceInterface: SurfaceInterface) : GLSurfaceView.Renderer {

    private val figureBuilder = FigureBuilder()
    private lateinit var shader :DynamicModelShader

    private var height = 0
    private var width = 0

    @Volatile
    var xAngle = 0f
    @Volatile
    var yAngle = 0f

    var scaleFactor = 1f

    var strideX = 0f
    var strideY = 0f

    //used to move and rotate objects around the word
    private val modelMatrix = FloatArray(16)
    //camera matrix - positions objects relative to our eyes
    private val viewMatrix = FloatArray(16)
    //project the world into 3d
    private val projectionMatrix = FloatArray(16)

    private val viewProjectionMatrix = FloatArray(16)

    private var MVPMatrix = FloatArray(16)

    //matrix that undo the effects of view and projection matrix
    private val invertedViewProjectionMatrix = FloatArray(16)

    private val lightPosInModelSpace = floatArrayOf(0.0f, 0.0f, 0.0f, 1.0f)

    private val frontLightModelMatrix = FloatArray(16)
    private val frontLightPosInWorldSpace = FloatArray(4)
    private val frontLightPosInEyeSpace = FloatArray(4)

    init {




    }

    fun handleTouch(x: Float, y: Float){


    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

        val bgColor = ColorRGB("#ffd7a6")
        GLES20.glClearColor(bgColor.RED, bgColor.GREEN, bgColor.BLUE, 0.0f)

        // Use culling to remove ic_back_black faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE)

        // Enable depth testing to remove drawing objects that are behind other objects
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

        // Position the eye in front of the origin.
        val eyeX = 0.0f
        val eyeY = 0.0f
        val eyeZ = -0.5f

        // We are looking toward the distance
        val lookX = 0.0f
        val lookY = 0.0f
        val lookZ = -5.0f

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0f
        val upY = 1.0f
        val upZ = 0.0f

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ)

        Matrix.translateM(modelMatrix, 0, 0f, 0f, 7.0f)

        shader = DynamicModelShader(surfaceInterface.getRenderActivityContext())


        figureBuilder.addObject(Cube(Point3d(0f, 0f, 0f), ColorRGB("#eb4034"), true))
        figureBuilder.build()
        figureBuilder.bindAttributesData()

    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 3.0f
        val far = 100.0f

        this.width = width
        this.height = height

        Matrix.orthoM(projectionMatrix, 0, left, ratio, bottom, top, near, far)

    }


    override fun onDrawFrame(gl: GL10?) {

        GLES20.glClear(GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        calculateModelMatrix()

        calculateLightMatrices(0f, 0f)

        calculateMVPMatrix()

        setUniforms()

        setScaleFactor()

        figureBuilder.draw(shader)

    }


    private fun calculateModelMatrix() {

        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -7.0f)

        //set user made rotation
        Matrix.rotateM(modelMatrix, 0, yAngle, 1f, 0f, 0f)
        Matrix.rotateM(modelMatrix, 0, xAngle, 0f, 1f, 0f)

    }

    fun calculateLightMatrices(xAngle: Float, yAngle: Float) {

        Matrix.setIdentityM(frontLightModelMatrix, 0)
        Matrix.translateM(frontLightModelMatrix, 0, 0.0f, 0.0f, -7.0f)
        //count all the light source position
        val lightDistance = 100f

        Matrix.translateM(frontLightModelMatrix, 0, 0.0f, 0.0f, lightDistance)
        Matrix.multiplyMV(frontLightPosInWorldSpace, 0, frontLightModelMatrix, 0, lightPosInModelSpace, 0)
        Matrix.multiplyMV(frontLightPosInEyeSpace, 0, viewMatrix, 0, frontLightPosInWorldSpace, 0)

    }

    fun setUniforms() {

        shader.useProgram()
        shader.setUniforms(modelMatrix, viewMatrix, projectionMatrix, frontLightPosInEyeSpace)


    }

    fun calculateMVPMatrix() {

        Matrix.multiplyMM(viewProjectionMatrix, 0, viewMatrix, 0, projectionMatrix, 0)
        Matrix.invertM(invertedViewProjectionMatrix, 0, viewProjectionMatrix, 0)
        Matrix.translateM(invertedViewProjectionMatrix, 0, 0f, 0f, -10f)
        MVPMatrix = getMVPMatrix(modelMatrix, viewMatrix, projectionMatrix)
    }

    private fun getMVPMatrix(modelMatrix: FloatArray, viewMatrix: FloatArray, projectionMatrix: FloatArray): FloatArray {

        val MVPMatrix = FloatArray(16)
        Matrix.multiplyMM(MVPMatrix, 0, viewMatrix, 0, modelMatrix, 0)
        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, MVPMatrix, 0)
        return MVPMatrix

    }

    private fun setScaleFactor() {
        shader.setScaleFactor(scaleFactor)
    }

}