package com.rootdown.dev.paging_v3_1.ui.dashboard


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
import com.rootdown.dev.paging_v3_1.Injection
import com.rootdown.dev.paging_v3_1.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import com.rootdown.dev.paging_v3_1.databinding.FragmentSearchReposBinding
import com.rootdown.dev.paging_v3_1.data.Repo
import com.rootdown.dev.paging_v3_1.ui.MainActivity


class SearchReposFragment: Fragment() {

    private lateinit var binding: FragmentSearchReposBinding
    private lateinit var state: String

    private val viewModel: SearchRepositoriesViewModel by lazy {
        val activity = requireNotNull(this.activity){
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, Injection.provideViewModelFactory(activity.application))
            .get(SearchRepositoriesViewModel::class.java)
    }

    var profile_ls: List<Repo> = emptyList()
    private val adapter = ReposAdapter{position -> onListItemClick(position)}
    private var searchJob: Job? = null

    override fun onResume() {
        super.onResume()

        // Set title bar
        (activity as MainActivity)
            .setActionBarTitle("")
    }

    private fun onListItemClick(position: Int) {
        val current_repo: Repo = profile_ls[position]
        val lat: Float = current_repo.lat.toFloat()
        val lng: Float = current_repo.lng.toFloat()
        val title: String = current_repo.company_name
        val action = SearchReposFragmentDirections.actionFragmentDashboardToFragmentMaps(lat,lng,title)
        this.findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        state = query.toString()
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchReposBinding.inflate(inflater)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        //search(state)
        initSearch()
        binding.retryButton.setOnClickListener { adapter.retry() }
        viewModel.x_profiles.observe(viewLifecycleOwner){
            profile_ls = it
        }
    }

    @ExperimentalPagingApi
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_profile, menu)
        //search(state)
        //initSearch(state)
        val search: MenuItem? = menu.findItem(R.id.profile_search)
        val searchView: SearchView = search?.actionView as SearchView
        searchView.queryHint = "Abbr. State, City, Dispensary Name"
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                state = query.toString()
                updateRepoListFromInput()
                return true
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }


    @ExperimentalPagingApi
    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
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
        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )
        adapter.addLoadStateListener { loadState ->
            // Only show the list if refresh succeeds.
            binding.list.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    this.activity,
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    @ExperimentalPagingApi
    private fun initSearch() {

        updateRepoListFromInput()

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.list.scrollToPosition(0) }
        }
    }

    @ExperimentalPagingApi
    private fun updateRepoListFromInput() {
        val x: String = state
        search(x.toString())
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "denver"
    }
}