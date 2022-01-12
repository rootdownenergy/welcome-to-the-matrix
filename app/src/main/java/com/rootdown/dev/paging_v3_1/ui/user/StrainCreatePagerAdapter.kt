package com.rootdown.dev.paging_v3_1.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


private const val ARG_OBJECT = "object"
class StrainCreatePagerAdapter(frag: Fragment) : FragmentStateAdapter(frag) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        val fragment = UserStrainsFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment
    }
}