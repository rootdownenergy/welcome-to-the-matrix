package com.rootdown.dev.paging_v3_1.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.rootdown.dev.paging_v3_1.R

/**
 * Adapter for the list of repositories.
 */
class ReposAdapter(private val onItemClicked: (position: Int) -> Unit): PagingDataAdapter<UiModel, ViewHolder>(UIMODEL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == R.layout.repo_view_item) {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.repo_view_item, parent, false)
            return RepoViewHolder(view, onItemClicked)
        } else {
            SeparatorViewHolder.create(parent)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UiModel.RepoItem -> R.layout.repo_view_item
            is UiModel.SeparatorItem -> R.layout.seperator_view_item
            null -> throw UnsupportedOperationException("Unknown view")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val uiModel = getItem(position)
        uiModel.let {
            when (uiModel) {
                is UiModel.RepoItem ->
                {
                    holder.itemView.setOnClickListener { x ->
                        val posPro = uiModel.repo.company_name
                        val action =
                            SearchReposFragmentDirections.actionNavigationDashboardToProfileDetailsFragment(posPro)
                        x.findNavController().navigate(action)
                    }
                    (holder as RepoViewHolder).bind(uiModel.repo)
                }
                is UiModel.SeparatorItem ->
                {
                    (holder as SeparatorViewHolder).bind(uiModel.description)
                }
            }
        }
    }


    companion object {
        private val UIMODEL_COMPARATOR = object : DiffUtil.ItemCallback<UiModel>() {
            override fun areItemsTheSame(oldItem: UiModel, newItem: UiModel): Boolean {
                return (oldItem is UiModel.RepoItem && newItem is UiModel.RepoItem &&
                        oldItem.repo.id == newItem.repo.id) ||
                        (oldItem is UiModel.SeparatorItem && newItem is UiModel.SeparatorItem &&
                                oldItem.description == newItem.description)
            }

            override fun areContentsTheSame(oldItem: UiModel, newItem: UiModel): Boolean =
                oldItem == newItem
        }
    }
}