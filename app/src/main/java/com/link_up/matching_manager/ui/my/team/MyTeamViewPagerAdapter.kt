package com.link_up.matching_manager.ui.my.team

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyTeamViewPagerAdapter (fragment : FragmentActivity) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MyTeamRecruitFragment()
            else -> MyTeamApplicationFragment()
        }
    }
}