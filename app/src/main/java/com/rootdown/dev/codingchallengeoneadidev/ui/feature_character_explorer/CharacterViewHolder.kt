package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rootdown.dev.codingchallengeoneadidev.R
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar

class CharacterViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {

    // local
    private val name: TextView = view.findViewById(R.id.repo_name)
    private val img: AppCompatImageView = view.findViewById(R.id.character_img)
    private var repo: BreakingBadChar? = null

    fun bind(repo: BreakingBadChar?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
        } else {
            showCharacter(repo)
        }
    }

    override fun onClick(p0: View?) {
        val position = absoluteAdapterPosition
        onItemClicked(position)
    }

    private fun showCharacter(repo: BreakingBadChar){
        this.repo = repo
        name.text = repo.name
        val url = repo.img
        Glide.with(img)
            .load(url)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(img)
    }
}