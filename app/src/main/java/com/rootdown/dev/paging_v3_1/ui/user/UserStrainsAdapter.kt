package com.rootdown.dev.paging_v3_1.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.paging_v3_1.R
import com.rootdown.dev.paging_v3_1.api.UserStrain
import com.rootdown.dev.paging_v3_1.data.DatabaseStrain

class UserStrainsAdapter(private val onItemClicked: (position: Int) -> Unit): RecyclerView.Adapter<UserStrainViewHolder>() {
    var data = listOf<UserStrain>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserStrainViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_strain_view_item, parent, false)
        return UserStrainViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: UserStrainViewHolder, position: Int) {
        val item = data[position]
    }

    override fun getItemCount() = data.size

    //override fun getItemCount(): Int {
       // return strains.size
    //}
}