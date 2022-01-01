package com.echoeyecodes.uiproject.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.echoeyecodes.uiproject.callbacks.RVCustomViewCallback

fun Int.convertToDp(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
        .toInt()
}


fun View.getRootViewOffset(): Int {
    val intArray = IntArray(2)
    this.getLocationOnScreen(intArray)
    return intArray[1]
}

@SuppressLint("ClickableViewAccessibility")
fun View.setOnLongPressListener(callback:RVCustomViewCallback) {
    val coordinates = Point()

    val screenPosition = IntArray(2)

    setOnTouchListener { v, event ->
        v.getLocationOnScreen(screenPosition)
        if (event.action == MotionEvent.ACTION_DOWN) {
            coordinates.set(
                event.x.toInt() + screenPosition[0],
                event.y.toInt() + screenPosition[1]
            )
        }else if (event.action == MotionEvent.ACTION_UP){
            callback.onRelease()
        }
        v.onTouchEvent(event)
    }
    setOnLongClickListener {
        callback.onLongPress(it, coordinates)
        true
    }
}