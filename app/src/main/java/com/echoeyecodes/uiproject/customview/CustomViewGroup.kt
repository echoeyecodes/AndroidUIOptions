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
import com.echoeyecodes.uiproject.callbacks.CustomViewGroupCallback
import com.echoeyecodes.uiproject.utils.*
import kotlin.math.*


class CustomViewGroup(context: Context, attributeSet: AttributeSet) :
    ViewGroup(context, attributeSet) {
    private var locations = ArrayList<ViewObject>()
    private val screenDimensions = getScreenSize()
    private val screenWidth = screenDimensions.width
    private val screenHeight = screenDimensions.height
    private val path = Path()
    private val paint = Paint()
    private var customViewGroupCallback: CustomViewGroupCallback? = null

    private var config = CustomViewGroupConfig.Builder().build()

    init {
        setWillNotDraw(false)
//        setConfig(
//            CustomViewGroupConfig.Builder().setBounds(
//                (screenWidth * 0.3f),
//                screenHeight * 0.4f,
//                screenWidth * 0.8f,
//                screenHeight * 0.8f
//            ).setCornerRadius(20f).setSpacing(70.convertToDp())
//                .setCoordinates(screenWidth / 2, screenHeight / 2).build()
//        )
    }

    fun setCustomViewGroupCallback(callback: CustomViewGroupCallback) {
        this.customViewGroupCallback = callback
    }

    fun setConfig(config: CustomViewGroupConfig) {
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

        path.reset()
        path.moveTo(0f, 0f)
        path.lineTo(screenWidth.toFloat(), 0f)
        path.lineTo(screenWidth.toFloat(), config.rectF.top)
        path.lineTo(config.rectF.right, config.rectF.top)

        path.addRoundRect(
            config.rectF,
            config.cornerRadius,
            config.cornerRadius,
            Path.Direction.CCW
        )

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
        super.onTouchEvent(event)
        val _x = event.x.toInt()
        val _y = event.y.toInt()

        return when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val location = getViewLocation(_x, _y)
                if (location != null) {
                    updateViewScale(location)
                } else {
                    resetViewScale()
                }
                true
            }
            MotionEvent.ACTION_UP -> {
                val location = getViewLocation(_x, _y)
                touchReleased(location)
                true
            }
            else -> false
        }
    }

    private fun getViewLocation(_x: Int, _y: Int): ViewObject? {
        return locations.find {
            isBetween(
                _x,
                _y,
                it.x,
                it.x + it.width,
                it.y,
                it.y + it.height
            )
        }
    }

    private fun updateViewScale(viewObject: ViewObject) {
        val child = getChildAt(viewObject.index)
        activateView(child)
        customViewGroupCallback?.onItemFocused(viewObject.index)
    }

    private fun resetViewScale() {
        locations.forEach {
            val child = getChildAt(it.index)
            deactivateView(child)
            customViewGroupCallback?.onInvalidLocationFocused()
        }
    }

    private fun touchReleased(location: ViewObject?) {
        if(location != null){
            customViewGroupCallback?.onItemSelected(location.index)
        }
        resetViewScale()
        customViewGroupCallback?.onItemsUnFocused()
    }

    private fun activateView(view: View) {
        view.isPressed = true
    }

    private fun deactivateView(view: View) {
        view.isPressed = false
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        measureChildren(0, 0)
        val count = childCount

        //radius should not be less than view width/height
        var angle = -(Math.PI / 2)
        val radius = config.spacing.convertToDp()
        val startCoordinates = Pair(config.cx, config.cy)
        val step = (2 * Math.PI) / 8

        for (i in 0 until count) {
            val child = getChildAt(i) as Circle
            val childWidth = child.layoutParams.width
            val childHeight = child.layoutParams.height

            setCircleColor(child, i)

            val _left = (startCoordinates.first + (radius * cos(angle)) - childWidth / 2).toInt()
            val _right = childWidth + _left
            val _top = (startCoordinates.second + (radius * sin(angle)) - childHeight / 2).toInt()
            val _bottom = childHeight + _top

            if ((screenWidth - startCoordinates.first) < radius) {
                angle -= step
            } else {
                angle += step
            }

            child.layout(_left, _top, _right, _bottom)
            cacheLocation(ViewObject(i, _left, _top, childWidth, childHeight))
        }

    }

    private fun cacheLocation(viewObject: ViewObject) {
        val index = locations.indexOfFirst { it.index == viewObject.index }
        if (index == -1) {
            locations.add(viewObject)
        } else {
            locations.removeAt(index)
            locations.add(viewObject)
        }
    }
}