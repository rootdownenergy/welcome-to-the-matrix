package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.codingchallengeoneadidev.R

class CharacterAdapter(private val onItemClicked: (position: Int) -> Unit): PagingDataAdapter<UiModel.CharItem, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.CharItem ->
                {
                    holder.itemView.setOnClickListener { x ->
                        val posPro = uiModel.repo.nickname
                        val action =
                            SearchCharacterFragmentDirections.actionNavigationHomeToCharacterDetailFragment()
                        x.findNavController().navigate(action)
                    }
                    (holder as CharacterViewHolder).bind(uiModel.repo)
                }
                else -> {throw UnsupportedOperationException("Unknown view")}
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == R.layout.repo_view_item) {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.repo_view_item, parent, false)
            return CharacterViewHolder(view, onItemClicked)
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.CharItem -> R.layout.repo_view_item
            null -> throw UnsupportedOperationException("Unknown view")
            else -> {throw UnsupportedOperationException("Unknown view")}
        }
    }

    // create obd for diffing
    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel.CharItem>() {

            override fun areItemsTheSame(
                oldItem: UiModel.CharItem,
                newItem: UiModel.CharItem
            ): Boolean {
                return (oldItem.repo.charId == newItem.repo.charId)
            }

            override fun areContentsTheSame(
                oldItem: UiModel.CharItem,
                newItem: UiModel.CharItem
            ): Boolean = oldItem == newItem
        }
    }
}