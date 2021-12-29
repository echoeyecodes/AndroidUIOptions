package com.echoeyecodes.uiproject.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.echoeyecodes.uiproject.R
import com.echoeyecodes.uiproject.customview.CustomViewGroup
import com.echoeyecodes.uiproject.databinding.LayoutDialogOptionsBinding
import com.echoeyecodes.uiproject.utils.AndroidUtilities
import com.echoeyecodes.uiproject.utils.CustomViewGroupConfig

class CustomViewDialogFragment : DialogFragment() {

    private var config:CustomViewGroupConfig? = null

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
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val binding = LayoutDialogOptionsBinding.inflate(inflater)
        val view =  binding.root

        config?.let { view.setConfig(it) }
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun sendMotionEventSignal(event: MotionEvent){
        (view as CustomViewGroup?)?.dispatchTouchEvent(event)
    }

    fun setCustomViewConfig(config: CustomViewGroupConfig):CustomViewDialogFragment {
        return apply { this.config = config }
    }
}