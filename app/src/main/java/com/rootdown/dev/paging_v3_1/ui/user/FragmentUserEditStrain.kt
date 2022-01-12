package com.rootdown.dev.paging_v3_1.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.rootdown.dev.paging_v3_1.databinding.FragmentUserEditStrainBinding
import com.rootdown.dev.paging_v3_1.ui.MainActivity
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FragmentUserEditStrain: Fragment() {
    private var _binding: FragmentUserEditStrainBinding? = null
    private val binding get() = _binding!!
    private val userViewModel: UserContentViewModel by viewModels()
    private var strid by Delegates.notNull<Int>()
    private lateinit var currStrainName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).supportActionBar?.title = ""
    }
    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as MainActivity)
            .setActionBarTitle("")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserEditStrainBinding.inflate(inflater, container, false)
        val view = binding.root
        val fab: View = binding.fab
        val img: ImageView = _binding!!.strainDetailImgId

        userViewModel.strainEditDetailed.observe(viewLifecycleOwner){
            currStrainName = it.strain_name
            strid = it.id
            val it_strain_img = it.strain_image
            val uri = "https://cdn.karmanomic.com/$it_strain_img"
            if(it_strain_img != null){
                Glide.with(this)
                    .load(uri)
                    .override(100, 600)
                    .fitCenter()
                    .into(img)
            }
            _binding!!.strainDetailNameId.text = it.strain_name
        }

        fab.setOnClickListener { view ->
            //val x = userViewModel.checkNullStrainId()
            val x: Int = strid
            lifecycleScope.launch {
                userViewModel.deleteUserStrain(x)
            }
            this.findNavController().navigateUp()
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}