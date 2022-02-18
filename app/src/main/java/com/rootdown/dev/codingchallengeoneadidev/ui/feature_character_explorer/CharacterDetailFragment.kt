package com.rootdown.dev.codingchallengeoneadidev.ui.feature_character_explorer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rootdown.dev.codingchallengeoneadidev.databinding.FragmentSearchCharacterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharacterDetailFragment: Fragment() {

    private lateinit var binding: FragmentSearchCharacterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCharacterBinding.inflate(inflater)
        return binding.root
    }
}