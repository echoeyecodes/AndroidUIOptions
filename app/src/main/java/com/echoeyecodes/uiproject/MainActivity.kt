package com.echoeyecodes.uiproject

import android.annotation.SuppressLint
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.uiproject.adapters.DefaultAdapter
import com.echoeyecodes.uiproject.callbacks.DefaultAdapterCallback
import com.echoeyecodes.uiproject.databinding.ActivityMainBinding
import com.echoeyecodes.uiproject.fragments.CustomViewDialogFragment
import com.echoeyecodes.uiproject.utils.CustomViewGroupConfig
import com.echoeyecodes.uiproject.utils.getRootViewOffset

class MainActivity : AppCompatActivity(), DefaultAdapterCallback {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var customViewDialogFragment: CustomViewDialogFragment

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        customViewDialogFragment =
            supportFragmentManager.findFragmentByTag(CustomViewDialogFragment.TAG) as CustomViewDialogFragment?
                ?: CustomViewDialogFragment.newInstance()
        recyclerView = binding.recyclerView
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        val adapter = DefaultAdapter(this)
        recyclerView.adapter = adapter

        adapter.submitList(listOf("", "", "", "", "", "", "", "", "", ""))
        setRecyclerViewListener()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setRecyclerViewListener(){
        recyclerView.setOnTouchListener { _, motionEvent ->
            customViewDialogFragment.sendMotionEventSignal(motionEvent)
        }
    }

    override fun onLongPress(view: View, point: Point) {
        val yOffset = binding.root.getRootViewOffset()

        val config = CustomViewGroupConfig.Builder().setSpacing(100)
            .setCoordinates(point.x, point.y - yOffset).setBounds(
                view.left.toFloat(),
                view.top.toFloat(),
                view.right.toFloat(),
                view.bottom.toFloat()
            ).build()
        if (!customViewDialogFragment.isAdded) {
            customViewDialogFragment.setCustomViewConfig(config)
                .show(supportFragmentManager, CustomViewDialogFragment.TAG)
        }
    }

    override fun onRelease() {
        if(customViewDialogFragment.isVisible){
            customViewDialogFragment.dismiss()
        }
    }

    override fun onClick() {

    }
}