package com.example.matching_manager.ui.team

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.matching_manager.R
import com.example.matching_manager.ui.team.menu.MyTeamFragment
import com.example.matching_manager.ui.team.menu.RecruitmentFragment

class TeamViewPagerAdapter(private val activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    private val fragments = listOf(
        TeamTab(MyTeamFragment.newInstance(), R.string.team_myteam),
        TeamTab(RecruitmentFragment.newInstance(), R.string.team_recruitment)
    )

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
