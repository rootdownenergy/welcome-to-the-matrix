package com.rootdown.dev.paging_v3_1.ui.user

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.paging_v3_1.Config
import com.rootdown.dev.paging_v3_1.R
import com.rootdown.dev.paging_v3_1.api.UserProfile

class UserProfileViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit):
    RecyclerView.ViewHolder(view), View.OnClickListener {
    private val name: TextView = view.findViewById(R.id.repo_name)
    private val description: TextView = view.findViewById(R.id.repo_description)
    private val language: TextView = view.findViewById(R.id.repo_language)
    private val btnMaps: Button = view.findViewById(R.id.btn_geo)
    private val txtLat: TextView = view.findViewById(R.id.txtLat)
    private val txtLng: TextView = view.findViewById(R.id.txtLng)
    private var currRepo: UserProfile? = null

    init {
        //view.setOnClickListener(this)
        btnMaps.setOnClickListener(this)
    }

    fun bind(repo: UserProfile?) {
        currRepo = repo
        if (repo == null) {
            Log.w("BIND$$", "CURRENT REPO NULL NO BINDING")
        } else {
            showRepoData(repo)
        }
    }
    override fun onClick(v: View?) {
        val position = absoluteAdapterPosition
        onItemClicked(position)
    }


    private fun showRepoData(repo: UserProfile) {
        this.currRepo = repo
        name.text = repo.company_name

        description.text = repo.company_description
        txtLat.text = repo.lat.toString()
        txtLng.text = repo.lng.toString()
        val xlat: Float = repo.lat.toFloat()
        val ylng: Float = repo.lng.toFloat()
        val currname: String = repo.company_name
        Config.lat_in = xlat
        Config.lng_in = ylng
        Config.curr_nm = currname
        // if the description is missing, hide the TextView
        var descriptionVisibility = View.GONE
        if (repo.company_description != null) {
            description.text = repo.company_description
            descriptionVisibility = View.VISIBLE
        }
        description.visibility = descriptionVisibility

        // if the language is missing, hide the label and the value
        var languageVisibility = View.GONE
        if (repo.city.isNotEmpty()) {
            val resources = this.itemView.context.resources
            language.text = resources.getString(R.string.city, repo.city)
            languageVisibility = View.VISIBLE
        }
        language.visibility = languageVisibility
    }

}