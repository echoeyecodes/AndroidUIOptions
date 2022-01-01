package com.echoeyecodes.uiproject.callbacks

import android.graphics.Point
import android.view.View

interface RVCustomViewCallback {
    fun onLongPress(view:View, point:Point)
    fun onRelease()
    fun onClick()
}