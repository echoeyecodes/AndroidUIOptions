package com.echoeyecodes.uiproject.utils

import android.content.res.Resources
import android.util.TypedValue

fun Int.convertToDp() : Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
        .toInt()
}

