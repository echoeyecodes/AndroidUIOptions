package com.echoeyecodes.uiproject.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.echoeyecodes.uiproject.utils.AndroidUtilities

class Circle(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    private val circlePaint = Paint()

    init {
        setCircleColor(Color.GREEN)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val circleWidth = (width/2).toFloat()
        val circleHeight = (height/2).toFloat()
        val circleRadius = circleWidth

        canvas.drawCircle(circleWidth, circleHeight, circleRadius, circlePaint)
    }

    fun setCircleColor(color:Int){
        circlePaint.color = color
    }
}