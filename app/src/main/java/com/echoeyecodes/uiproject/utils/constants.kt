package com.echoeyecodes.uiproject.utils

import android.content.res.Resources

class Dimensions(val width:Int, val height:Int)

fun getScreenSize():Dimensions{
    return Dimensions(Resources.getSystem().displayMetrics.widthPixels, Resources.getSystem().displayMetrics.heightPixels)
}
