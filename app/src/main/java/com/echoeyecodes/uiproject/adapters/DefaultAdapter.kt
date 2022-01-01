package com.echoeyecodes.uiproject.adapters

import android.graphics.Color
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.uiproject.R
import com.echoeyecodes.uiproject.callbacks.DefaultAdapterCallback
import com.echoeyecodes.uiproject.callbacks.RVCustomViewCallback
import com.echoeyecodes.uiproject.utils.DefaultItemCallback
import com.echoeyecodes.uiproject.utils.setOnLongPressListener

class DefaultAdapter(private val callback:DefaultAdapterCallback) :
    ListAdapter<String, DefaultAdapter.DefaultAdapterViewHolder>(DefaultItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultAdapterViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_adapter_item, parent, false)
        return DefaultAdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: DefaultAdapterViewHolder, position: Int) {
            holder.bind(position)
    }

    inner class DefaultAdapterViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(position: Int) {
            view.setBackgroundColor(
                if (position % 2 == 0) {
                    Color.GREEN
                } else {
                    Color.YELLOW
                }
            )

            view.setOnLongPressListener(callback)
        }
    }
}