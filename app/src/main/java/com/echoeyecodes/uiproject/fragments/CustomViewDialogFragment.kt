package com.echoeyecodes.uiproject.fragments

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.echoeyecodes.uiproject.callbacks.CustomViewGroupCallback
import com.echoeyecodes.uiproject.callbacks.RVCustomViewCallback
import com.echoeyecodes.uiproject.customview.CustomViewGroup
import com.echoeyecodes.uiproject.databinding.LayoutDialogOptionsBinding
import com.echoeyecodes.uiproject.utils.AndroidUtilities
import com.echoeyecodes.uiproject.utils.CustomViewGroupConfig
import com.echoeyecodes.uiproject.utils.getScreenSize

class CustomViewDialogFragment : DialogFragment(), CustomViewGroupCallback {

    private lateinit var customViewGroup: CustomViewGroup
//    private val binding by lazy { LayoutDialogOptionsBinding.inflate(layoutInflater) }
    private lateinit var textView:TextView
    private var config:CustomViewGroupConfig? = null
    private val screenWidth = getScreenSize().width

    companion object {
        const val TAG = "CUSTOM_VIEW_DIALOG_FRAGMENT"
        fun newInstance() = CustomViewDialogFragment()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        dialog?.window?.setGravity(Gravity.TOP or Gravity.LEFT)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val binding = LayoutDialogOptionsBinding.inflate(layoutInflater)
        textView = binding.btnText
        customViewGroup = binding.customViewGroup

        binding.customViewGroup.setCustomViewGroupCallback(this)
        config?.let { binding.customViewGroup.setConfig(it) }
        determineTextViewLocation()

        val view =  binding.root
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun sendMotionEventSignal(event: MotionEvent):Boolean{
        return if(isVisible){
            customViewGroup.dispatchTouchEvent(event)
        }else{
            false
        }
    }

    fun setCustomViewConfig(config: CustomViewGroupConfig):CustomViewDialogFragment {
        return apply { this.config = config }
    }

    private fun determineTextViewLocation(){
        config?.let {
            if(it.cx < (screenWidth/2)){
                (textView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.END
            }else{
                (textView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.START
            }
        }
    }

    override fun onItemFocused(index: Int) {
        textView.text = index.toString()
    }

    override fun onItemsUnFocused() {
        textView.text = ""
        (requireContext() as RVCustomViewCallback).onRelease()
    }

    override fun onInvalidLocationFocused() {
        textView.text = ""
    }

    override fun onItemSelected(index: Int) {
        AndroidUtilities.log("Selected index $index")
    }
}