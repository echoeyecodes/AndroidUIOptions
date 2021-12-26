package com.echoeyecodes.uiproject.customview


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.echoeyecodes.uiproject.R
import com.echoeyecodes.uiproject.utils.CustomViewGroupConfig
import com.echoeyecodes.uiproject.utils.ViewObject
import com.echoeyecodes.uiproject.utils.convertToDp
import com.echoeyecodes.uiproject.utils.getScreenSize
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class CustomViewGroup(context: Context, attributeSet: AttributeSet) :
    ViewGroup(context, attributeSet) {
    private val locations = ArrayList<ViewObject>()
    private val screenDimensions = getScreenSize()
    private val screenWidth = screenDimensions.width
    private val screenHeight = screenDimensions.height
    private val path = Path()
    private val paint = Paint()

    private var config = CustomViewGroupConfig.Builder().build()

    init {
        setWillNotDraw(false)
        setConfig(CustomViewGroupConfig.Builder().setBounds((screenWidth*0.3f),screenHeight*0.4f,screenWidth*0.8f,screenHeight*0.8f).setCornerRadius(20f).setSpacing(70.convertToDp()).setCoordinates(screenWidth/2, screenHeight/2).build())
    }

    fun setConfig(config: CustomViewGroupConfig){
        this.config = config
    }

    private fun setCircleColor(view: Circle, position: Int) {
        when (position) {
            1 -> {
                view.setCircleColor(Color.GREEN)
            }
            2 -> {
                view.setCircleColor(Color.WHITE)
            }
            3 -> {
                view.setCircleColor(Color.RED)
            }
            else -> {
                view.setCircleColor(Color.YELLOW)
            }
        }
    }

    private fun isBetween(
        valueX: Int,
        valueY: Int,
        fromX: Int,
        toX: Int,
        fromY: Int,
        toY: Int
    ): Boolean {
        return ((valueX in fromX..toX) && (valueY in fromY..toY))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path.moveTo(0f,0f)
        path.lineTo(screenWidth.toFloat(), 0f)
        path.lineTo(screenWidth.toFloat(), config.rectF.top)
        path.lineTo(config.rectF.right, config.rectF.top)

        path.addRoundRect(config.rectF, config.cornerRadius,config.cornerRadius, Path.Direction.CCW)

        path.moveTo(screenWidth.toFloat(), config.rectF.top)
        path.lineTo(screenWidth.toFloat(), screenHeight.toFloat())
        path.lineTo(0f, screenHeight.toFloat())
        path.lineTo(0f, 0f)
        path.close()

        paint.color = config.shadowColor
        canvas.drawPath(path, paint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val _x = event.x.toInt()
        val _y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val location = locations.find {
                    isBetween(
                        _x,
                        _y,
                        it.x,
                        it.x + it.width,
                        it.y,
                        it.y + it.height
                    )
                }
                if (location != null) {
                    updateViewScale(location)
                } else {
                    resetViewScale()
                }
            }
            MotionEvent.ACTION_UP -> {
                resetViewScale()
            }
        }
        return true
    }

    private fun updateViewScale(viewObject: ViewObject) {
        val textView = findViewById<TextView>(R.id.btn_text)

        locations.forEach {
            if (it.index == viewObject.index) {
                val child = getChildAt(it.index)
                activateView(child)

                textView.text = it.index.toString()
            } else {
                val child = getChildAt(it.index)
                deactivateView(child)
            }
        }
    }

    private fun resetViewScale() {
        val textView = findViewById<TextView>(R.id.btn_text)

        locations.forEach {
            val child = getChildAt(it.index)
            deactivateView(child)
            textView.text = ""
        }
    }

    private fun activateView(view: View) {
        view.isPressed = true
    }

    private fun deactivateView(view: View) {
        view.isPressed = false
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        measureChildren(0,0)
        val count = childCount

        //radius should not be less than view width/height
        var angle = 0.0
        val spacing = config.spacing
        val startCoordinates = getCircleCoordinates(config.cx, config.cy)

        for (i in 0 until count) {
            val child = getChildAt(i)
            if (child is Circle) {
                val childWidth = child.layoutParams.width
                val childHeight = child.layoutParams.height

                setCircleColor(child, i)

                val _left = startCoordinates.first + ((spacing * sin(angle)).toInt())
                val _right = childWidth + _left
                val _top = startCoordinates.second - (spacing * cos(angle)).toInt()
                val _bottom = childHeight + _top

                angle -= child.layoutParams.width * 2

                child.layout(_left, _top, _right, _bottom)
                locations.add(ViewObject(i, _left, _top, childWidth, childHeight))
            } else if (child is TextView) {

                val childWidth = child.measuredWidth
                val childHeight = child.measuredHeight

                //if we are touching left hand side, position text at right hand
                //and vice versa
                val _left = if(startCoordinates.first <= screenWidth/2){
                    screenWidth - childWidth - 10.convertToDp()
                }else{
                    10.convertToDp()
                }

                val _right = childWidth + _left
                val _top = 200
                val _bottom = childHeight + _top

                child.layout(_left, _top, _right, _bottom)
            }
        }

    }

    private fun getCircleCoordinates(valueX: Int, valueY: Int): Pair<Int, Int> {
        val radius = config.spacing

        val yRadius = radius * 3
        val xRadius = radius * 2

        var blastRadiusX = min(valueX, screenWidth)

        //start button at the top should be above thumb
        var blastRadiusY = min(max(valueY - 50, 50), screenHeight)

        if (blastRadiusX < xRadius) {
            blastRadiusX += (radius - blastRadiusX)
        }else if (blastRadiusX > screenWidth /2 && (screenWidth - blastRadiusX) < xRadius) {
            blastRadiusX -= ((xRadius) - (screenWidth - blastRadiusX))
        }
        if(blastRadiusY > screenHeight && (screenHeight - blastRadiusY) < yRadius){
            blastRadiusY -= yRadius - (blastRadiusY - (screenHeight - yRadius))
        }

        return Pair(blastRadiusX, blastRadiusY)
    }
}