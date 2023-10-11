package com.example.matching_manager.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MainActivityBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private val binding by lazy { MainActivityBinding.inflate(layoutInflater) }
    private val viewPagerAdapter by lazy { MainViewPagerAdapter(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        //view pager adapter
        binding.viewPager.adapter = viewPagerAdapter

        // TabLayout x ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        //TabLayout icon 설정
        tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_home_grey)
        tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_match_grey)
        tabLayout.getTabAt(2)?.setIcon(R.drawable.ic_calendar_grey)
        tabLayout.getTabAt(3)?.setIcon(R.drawable.ic_team_grey)
        tabLayout.getTabAt(4)?.setIcon(R.drawable.ic_my_grey)

    }
}