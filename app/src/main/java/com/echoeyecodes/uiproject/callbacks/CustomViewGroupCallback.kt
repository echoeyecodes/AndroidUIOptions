package com.echoeyecodes.uiproject.callbacks

interface CustomViewGroupCallback {
    fun onItemFocused(index:Int)
    fun onItemsUnFocused()
    fun onInvalidLocationFocused()
    fun onItemSelected(index: Int)
}