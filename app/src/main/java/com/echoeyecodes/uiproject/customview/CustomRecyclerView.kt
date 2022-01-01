package com.echoeyecodes.uiproject.customview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

//Thank you soooo VERY MUCH @iloo😭😭
//https://stackoverflow.com/a/68828243/9781233
class CustomRecyclerView(context: Context, attributeSet: AttributeSet) :
    RecyclerView(context, attributeSet) {

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val eventConsumed = super.onInterceptTouchEvent(e)

        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (scrollState == SCROLL_STATE_DRAGGING) {
                    stopScroll()
                    return false
                }
            }
        }
        return eventConsumed
    }
}