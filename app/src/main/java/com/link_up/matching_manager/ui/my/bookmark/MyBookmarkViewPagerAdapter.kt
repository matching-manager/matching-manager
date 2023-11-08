package com.link_up.matching_manager.ui.my.bookmark

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyBookmarkViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyBookmarkMatchFragment()
            1 -> MyBookmarkRecruitFragment()
            else -> MyBookmarkApplicationFragment()
        }
    }
}