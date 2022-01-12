package com.rootdown.dev.paging_v3_1.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.paging_v3_1.R

class SeparatorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val description: TextView = view.findViewById(R.id.separator_description)

    fun bind(separatorText: String) {
        description.text = ""
    }

    companion object {
        fun create(parent: ViewGroup): SeparatorViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.seperator_view_item, parent, false)
            return SeparatorViewHolder(view)
        }
    }
}