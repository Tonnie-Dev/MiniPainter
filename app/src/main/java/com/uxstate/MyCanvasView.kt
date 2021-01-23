package com.uxstate

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

//stroke width
private const val STROKE_WIDTH = 12f // has to be a float
//Create a custom view and passing a context
class MyCanvasView(context: Context) : View(context) {
    //member variables for the Canvas and Bitmap
    //Canvas and Bitmap for caching what has been drawn before
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitMap: Bitmap //for caching what had been drawn before


    //initialize background color variable
    private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)

    //drawColor holds the color to draw with
    private val drawColor = ResourcesCompat.getColor(resources, R.color.colorPaint, null)

    //paint object for styling

    private val paint = Paint().apply {
        //drawing color
        color = drawColor
        //smooths out edges of what is drawn without affecting the shape
        isAntiAlias = true

        //Dithering affects how colors with higher-precision that the device are down-sampled
        isDither = true

        style = Paint.Style.STROKE //default:FILL
        strokeJoin = Paint.Join.ROUND //default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH //default: Hairline -width (really thin)
    }


    /*motion touch event X and motion touch event Y variables for
    caching the x and y coordinates of the current touch event*/

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f


    /*variables to cache the latest x and y values - after the
    user stops moving and lifts off their touch, these will be
    the starting points for the next path*/
    private var currentX = 0f
    private var currentY = 0f

    //path stores the path that is drawn when following the user's touch on the screen
    private val path = Path()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        //recycle extraBitmap before creating a new one to avoid memory leak
        if (::extraBitMap.isInitialized) {
            extraBitMap.recycle()
        }

        //create an instance of bitmap with new width and height

        /*ARGB_8888 stores each color in four bytes and is recommended*/
        extraBitMap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        //create a Canvas instance from Bitmap and assign it to extraCanvas
        extraCanvas = Canvas(extraBitMap)

        //specify the background color in which to fill extraCanvas
        extraCanvas.drawColor(backgroundColor)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //args - bitMap, x, y coordinates(pixels for the top left corner, paint to be set later
        canvas?.drawBitmap(extraBitMap, 0f, 0f, null)


        /*canvas passed to onDraw and used by the system to display the bitmap is different from the one
        created on onSizeChanged()*/
    }

/*override onTouchEvent method to cache the x and y coordinates of the passed in events*/
    override fun onTouchEvent(event: MotionEvent): Boolean {

    motionTouchEventX = event.x
    motionTouchEventY = event.y


    //when expression to handle motion events
    when(event.action){

        MotionEvent.ACTION_DOWN -> touchStart()
        MotionEvent.ACTION_MOVE -> touchMove()
        MotionEvent.ACTION_UP -> touchUp()
    }


        return super.onTouchEvent(event)
    }
//called when the user first touches the screen
    private fun touchStart(){

        path.reset()
    path.moveTo(motionTouchEventX, motionTouchEventY)

    currentX = motionTouchEventX
    currentY = motionTouchEventY
    }
    private fun touchMove(){}
    private fun touchUp(){}

}