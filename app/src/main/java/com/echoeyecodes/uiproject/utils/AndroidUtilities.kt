package com.echoeyecodes.uiproject.utils


import android.content.Context
import android.util.Log
import android.widget.Toast

class AndroidUtilities{

    companion object{
        fun showToastMessage(context: Context, message:String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        fun log(message: String) = Log.d("CARRR", message)
    }

}
