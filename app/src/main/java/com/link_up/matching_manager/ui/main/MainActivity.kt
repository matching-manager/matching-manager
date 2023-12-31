package com.link_up.matching_manager.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MainActivityBinding
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
        viewPager.offscreenPageLimit = 4

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

        val tabTitle = "CALENDAR"
        val spannableString = SpannableString(tabTitle)
        spannableString.setSpan(
            RelativeSizeSpan(0.8F), // 크기 조절
            0, // 시작 인덱스
            tabTitle.length, // 끝 인덱스
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tabLayout.getTabAt(2)?.text = spannableString

    }
    override fun onBackPressed() {
//        super.onBackPressed()
    }


    fun navigateToMatch() = with(binding) {
        viewPager.currentItem = 1
    }
}