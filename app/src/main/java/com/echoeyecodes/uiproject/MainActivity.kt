package com.echoeyecodes.uiproject

import android.annotation.SuppressLint
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.uiproject.adapters.DefaultAdapter
import com.echoeyecodes.uiproject.callbacks.DefaultAdapterCallback
import com.echoeyecodes.uiproject.databinding.ActivityMainBinding
import com.echoeyecodes.uiproject.fragments.CustomViewDialogFragment
import com.echoeyecodes.uiproject.utils.AndroidUtilities
import com.echoeyecodes.uiproject.utils.CustomViewGroupConfig
import com.echoeyecodes.uiproject.utils.convertToDp

class MainActivity : AppCompatActivity(), DefaultAdapterCallback {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var recyclerView: RecyclerView
    private lateinit var customViewDialogFragment: CustomViewDialogFragment

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
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun restoreTouchListener(){
        recyclerView.setOnTouchListener { v, event -> v.onTouchEvent(event) }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onItemLongPress(view: View, point: Point) {
        val config = CustomViewGroupConfig.Builder().setSpacing(70.convertToDp())
            .setCoordinates(point.x, point.y).setBounds(
                view.left.toFloat(),
                view.top.toFloat(),
                view.right.toFloat(),
                view.bottom.toFloat()
            ).build()
        customViewDialogFragment.setCustomViewConfig(config)
            .show(supportFragmentManager, CustomViewDialogFragment.TAG)
        recyclerView.setOnTouchListener { v, e ->
            //find a way to return default touch listener to recyclerview after fragment dropped
            customViewDialogFragment.sendMotionEventSignal(e)
            true
        }
    }
}