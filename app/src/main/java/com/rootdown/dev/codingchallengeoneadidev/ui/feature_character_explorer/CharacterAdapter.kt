package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootdown.dev.codingchallengeoneadidev.R
import com.rootdown.dev.codingchallengeoneadidev.viewmodel.feature_character_explorer.UiModel

class CharacterAdapter(private val onItemClicked: (position: Int) -> Unit): PagingDataAdapter<UiModel, RecyclerView.ViewHolder>(UIMODEL_COMPARATOR) {

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
                is UiModel.SeparatorItem ->
                {
                    (holder as SeparatorViewHolder).bind(uiModel.description)
                }
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
            is UiModel.SeparatorItem -> R.layout.seperator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    // create obd for diffing
    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.CharItem && newItem is UiModel.CharItem &&
                        oldItem.repo.charId == newItem.repo.charId) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}