package com.rootdown.dev.paging_v3_1.ui.user

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.airbnb.epoxy.EpoxyRecyclerView
import com.rootdown.dev.paging_v3_1.R
import com.rootdown.dev.paging_v3_1.api.UserProfile
import com.rootdown.dev.paging_v3_1.databinding.FragmentUserProfilesBinding
import com.rootdown.dev.paging_v3_1.profile
import com.rootdown.dev.paging_v3_1.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfilesFragment: Fragment() {

    private lateinit var binding: FragmentUserProfilesBinding
    private val viewModel: UserContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        exitTransition = inflater.inflateTransition(R.transition.fade)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.title = "SAVED STORES"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfilesBinding.inflate(inflater)
        val epoxyView: EpoxyRecyclerView = binding.userProfileList
        viewModel.updatedProfile.observe(viewLifecycleOwner){ profiles ->
            profiles?.let {
                setupRecyclerView(it, epoxyView)
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = "SAVED STORES"
    }

    private fun setupRecyclerView(x: List<UserProfile>, epoxy: EpoxyRecyclerView){
        epoxy.withModels {
            x.forEach { profile ->
                profile {
                    id(profile.id)
                    profile(profile)
                    clickListener { x ->
                        //Toast.makeText(activity, "Calls Icon Click ${profile.toString()}", Toast.LENGTH_LONG).show()
                        onListItemClick(profile.id)
                    }
                }
            }
        }
    }

    private fun onListItemClick(position: Long) {
        val x: Int = position.toInt()
        //val current_repo: UserProfile = profile_ls[x]
        this.findNavController().navigate(
            R.id.action_nav_user_profiles_to_edit_profile,
            bundleOf("profileId" to position)
        )
    }

}