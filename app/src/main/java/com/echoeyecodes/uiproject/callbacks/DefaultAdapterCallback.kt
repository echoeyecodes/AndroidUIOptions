package com.echoeyecodes.uiproject.callbacks

import android.graphics.Point
import android.view.MotionEvent
import android.view.View

interface DefaultAdapterCallback {
    fun onItemLongPress(view:View, point:Point)
}