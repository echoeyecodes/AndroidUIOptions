package com.echoeyecodes.uiproject.utils

import android.graphics.Color
import android.graphics.RectF

class CustomViewGroupConfig private constructor(val spacing: Int, val cx:Int, val cy:Int, val cornerRadius: Float, val shadowColor:Int, val rectF: RectF) {

    class Builder(){

        private var spacing = 0
        private var cx = 0
        private var cy = 0
        private var cornerRadius = 0f
        private var rectF:RectF = RectF()
        private var shadowColor = Color.parseColor("#000000")

        fun setSpacing(spacing:Int) = apply { this.spacing = spacing }
        fun setCoordinates(x:Int, y: Int) = apply {
            this.cx = x; this.cy = y
        }
        fun setShadowColor(color:Int) = apply { this.shadowColor = color }
        fun setCornerRadius(radius:Float) = apply { this.cornerRadius = radius }
        fun setBounds(left:Float, top:Float, right:Float, bottom:Float) = apply { this.rectF = RectF(left, top, right, bottom) }
        fun build() = CustomViewGroupConfig(spacing, cx, cy, cornerRadius, shadowColor, rectF)

    }
}