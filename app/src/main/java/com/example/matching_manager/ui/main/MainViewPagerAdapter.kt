package com.example.matching_manager.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matching_manager.R
import com.example.matching_manager.ui.calender.CalendarFragment
import com.example.matching_manager.ui.home.home.HomeFragment
import com.example.matching_manager.ui.match.MatchFragment
import com.example.matching_manager.ui.my.my.MyFragment
import com.example.matching_manager.ui.team.TeamFragment

class MainViewPagerAdapter(private val activity: FragmentActivity) :
    FragmentStateAdapter(activity) {
    private val fragments = ArrayList<MainTabs>()

    init {
        fragments.add(
            MainTabs(HomeFragment.newInstance(), R.string.main_home)
        )
        fragments.add(
            MainTabs(MatchFragment.newInstance(), R.string.main_match)
        )
        fragments.add(
            MainTabs(CalendarFragment.newInstance(), R.string.main_calendar)
        )
        fragments.add(
            MainTabs(TeamFragment.newInstance(), R.string.main_team)
        )
        fragments.add(
            MainTabs(MyFragment.newInstance(), R.string.main_mypage)
        )
    }

    fun getTitle(position: Int): Int {
        return fragments[position].title
    }

    //화면의 갯수
    override fun getItemCount(): Int {
        return fragments.size
    }

    //프래그먼트를 정의해 화면에 꼿아주는것
    override fun createFragment(position: Int): Fragment {
        return fragments[position].fragment
    }

}
