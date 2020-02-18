package com.vlad.dino3d

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import com.vlad.dino3d.utils.AntialiasingConfigurator
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class GameSurfaceView : GLSurfaceView, SurfaceInterface{

    lateinit var renderer : GameRenderer

    private val scaleFactor = 1f
    private var twoPointersDetected = false

    private val maxScale = 5f
    private val minScale = 0.5f

    private val firstPointerIndex = 0
    private val secondPointerIndex = 1
    private var pointerIndex = -1

    private var touchCenterX = 0f
    private var touchCenterY = 0f

    private var previousTouchCenterX = 0f
    private var previousTouchCenterY = 0f

    private var screenWidth = 0
    private var screenHeight = 0

    private var startingDistanceBetweenPointers = 0f
    private val movingSensitivity = 15f

    private val SCALE = 1
    private val MOVE = 2

    private val TOUCH_SCALE_FACTOR = 180.0f / 900
    private val ROTATE_START_POINT = 10f
    private var mPreviousX = 0f
    private var mPreviousY = 0f

    private var startX = 0f
    private var startY = 0f

    private var currentAction = MOVE

    //the figure will start scale when distance between pointers increases by this parameter %
    //0.1f means distance between fingers should dbe increased ast least by 10% of the screen width
    private val twoPointersMovementScaleBound = 0.1f
    private var twoPointersMovementDistance = x * twoPointersMovementScaleBound

    private val mScaleDetector: ScaleGestureDetector by lazy{ScaleGestureDetector(context, PinchListener())}

    constructor(context: Context?) : super(context) {setupRenderer()}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {setupRenderer()}

    private fun setupRenderer(){

        setEGLContextClientVersion(3)
        renderer = GameRenderer(this)

        screenWidth = context.resources.displayMetrics.widthPixels
        screenHeight = context.resources.displayMetrics.heightPixels

        twoPointersMovementDistance = screenWidth * twoPointersMovementScaleBound

        setEGLConfigChooser(AntialiasingConfigurator())

        setRenderer(renderer)

    }

    override fun getRenderActivityContext(): Context {
        return context
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.pointerCount > 1) {
            val xPointer1 = e.getX(firstPointerIndex).toInt()
            val yPointer1 = e.getY(firstPointerIndex).toInt()
            val xPointer2 = e.getX(secondPointerIndex).toInt()
            val yPointer2 = e.getY(secondPointerIndex).toInt()
            var distanceBetweenPointers = 0f
            if (e.actionMasked == MotionEvent.ACTION_MOVE) {
                twoPointersDetected = true
                touchCenterX = (xPointer2 + xPointer1) / 2f
                touchCenterY = (yPointer2 + yPointer1) / 2f
                if (startingDistanceBetweenPointers == 0f) {
                    startingDistanceBetweenPointers = sqrt(
                        (xPointer2 - xPointer1).toDouble().pow(2.0) +
                                (yPointer2 - yPointer1).toDouble().pow(2.0)
                    ).toFloat()
                    previousTouchCenterX = touchCenterX
                    previousTouchCenterY = touchCenterY
                }
                distanceBetweenPointers = Math.sqrt(
                    (xPointer2 - xPointer1).toDouble().pow(2.0) + (yPointer2 - yPointer1).toDouble().pow(2.0)
                ).toFloat()
            }
            if (twoPointersDetected) {
                currentAction =
                    if (Math.abs(distanceBetweenPointers - startingDistanceBetweenPointers) > twoPointersMovementDistance) SCALE else MOVE

                val touchCenterDX: Float = touchCenterX - previousTouchCenterX
                val touchCenterDY: Float = touchCenterY - previousTouchCenterY
                renderer.strideX = renderer.strideX + touchCenterDX / screenWidth * movingSensitivity
                renderer.strideY = renderer.strideY + touchCenterDY / screenHeight * movingSensitivity

                previousTouchCenterX = touchCenterX
                previousTouchCenterY = touchCenterY
                mScaleDetector.onTouchEvent(e)
            }
        } else {
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    twoPointersDetected = false
                    pointerIndex = if (pointerIndex == -1) e.actionIndex else pointerIndex
                    val pointerIndex = e.actionIndex
                    mPreviousX = e.getX(pointerIndex)
                    mPreviousY = e.getY(pointerIndex)
                    startX = mPreviousX
                    startY = mPreviousY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (!twoPointersDetected) {

                        val x = e.getX(pointerIndex)
                        val y = e.getY(pointerIndex)

                        val dx: Float = x - mPreviousX
                        val dy: Float = y - mPreviousY

                        renderer.xAngle = renderer.xAngle + dx * TOUCH_SCALE_FACTOR
                        renderer.yAngle = renderer.yAngle + dy * TOUCH_SCALE_FACTOR

                        mPreviousX = x
                        mPreviousY = y
                        requestRender()

                    }

                }
                MotionEvent.ACTION_UP -> {
                    resetPointersValues()
                    val x = e.getX(pointerIndex)
                    val y = e.getY(pointerIndex)
                    val dx: Float = x - startX
                    val dy: Float = y - startY
                    if (Math.abs(dx) < ROTATE_START_POINT && Math.abs(dy) < ROTATE_START_POINT) {
                        val normalizedX =
                            e.x / width.toFloat() * 2 - 1
                        val normalizedY =
                            -(e.y / height.toFloat() * 2 - 1)
                        renderer.handleTouch(normalizedX, normalizedY)
                    }
                }
            }
        }
        return true
    }

    private fun resetPointersValues() {
        startingDistanceBetweenPointers = 0f
        previousTouchCenterX = 0f
        previousTouchCenterY = 0f
    }

    inner class PinchListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            renderer.scaleFactor =
                max(minScale, min(renderer.scaleFactor * detector.scaleFactor, maxScale))
            twoPointersDetected = true
            return true
        }
    }


}