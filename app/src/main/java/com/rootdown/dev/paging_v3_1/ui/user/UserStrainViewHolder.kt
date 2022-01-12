package com.rootdown.dev.paging_v3_1.ui.user

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.paging_v3_1.R
import com.rootdown.dev.paging_v3_1.api.UserStrain

class UserStrainViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {
    private val strain_name: ImageView = view.findViewById(R.id.user_strain)


    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}