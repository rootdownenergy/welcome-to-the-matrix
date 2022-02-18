package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.codingchallengeoneadidev.R
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar

class CharacterViewHolder(view: View, private val onItemClicked: (position: Int) -> Unit): RecyclerView.ViewHolder(view), View.OnClickListener {

    // local
    private val name: TextView = view.findViewById(R.id.repo_name)
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
    }
}