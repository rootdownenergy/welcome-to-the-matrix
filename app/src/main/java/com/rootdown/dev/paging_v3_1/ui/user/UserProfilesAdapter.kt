package com.rootdown.dev.paging_v3_1.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.paging_v3_1.R
import com.rootdown.dev.paging_v3_1.api.UserProfile


class UserProfilesAdapter(private val onItemClicked: (position: Int) -> Unit):
    RecyclerView.Adapter<UserProfileViewHolder>() {
    var data = listOf<UserProfile>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProfileViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.user_profile_view_item, parent, false)
        return UserProfileViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: UserProfileViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount() = data.size

}