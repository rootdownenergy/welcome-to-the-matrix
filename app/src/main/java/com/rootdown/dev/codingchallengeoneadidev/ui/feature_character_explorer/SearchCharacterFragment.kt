package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.rootdown.dev.codingchallengeoneadidev.Injection
import com.rootdown.dev.codingchallengeoneadidev.R
import com.rootdown.dev.codingchallengeoneadidev.data.feature_character_explorer.db.BreakingBadChar
import com.rootdown.dev.codingchallengeoneadidev.databinding.FragmentSearchCharacterBinding
import com.rootdown.dev.codingchallengeoneadidev.ui.MainActivity
import com.rootdown.dev.codingchallengeoneadidev.viewmodel.feature_character_explorer.CharacterExplorerViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class SearchCharacterFragment: Fragment() {

    private lateinit var binding: FragmentSearchCharacterBinding
    private lateinit var state: String

    private val viewModel: CharacterExplorerViewModel by lazy {
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, Injection.provideViewModelFactory(activity.application))
            .get(CharacterExplorerViewModel::class.java)
    }

    // list of character
    private var characterLs: List<BreakingBadChar> = emptyList()
    private val adapter = CharacterAdapter{position -> onListItemClick(position)}
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        state = query
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCharacterBinding.inflate(inflater)
        return binding.root
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initSearch()
        binding.retryButton.setOnClickListener { adapter.retry() }
        viewModel.x_profiles.observe(viewLifecycleOwner){
            Log.w("$$**", "IN onViewCreated")
            characterLs = it
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_character, menu)
        val search: MenuItem? = menu.findItem(R.id.character_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Character Name"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                state = query.toString()
                updateRepoLsIn()
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    @ExperimentalPagingApi
    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, state)
    }

    private fun initAdapter() {
        binding.characterRV.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            binding.characterRV.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this.activity,
                    "${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    @ExperimentalPagingApi
    private fun initSearch() {
        updateRepoLsIn()
        lifecycleScope.launch {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.characterRV.scrollToPosition(0) }
        }
    }

    @ExperimentalPagingApi
    private fun updateRepoLsIn() {
        val x: String = state
        search(x)
    }

    private fun onListItemClick(position: Int) {
        val action =
            SearchCharacterFragmentDirections.actionNavigationHomeToCharacterDetailFragment()
        this.findNavController().navigate(action)
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = ""
    }
}