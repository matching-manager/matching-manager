package com.link_up.matching_manager.ui.my.bookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.link_up.matching_manager.databinding.MyBookmarkActivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyBookmarkActivity : AppCompatActivity() {
    private lateinit var binding : MyBookmarkActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyBookmarkActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    private fun initView() = with(binding) {
        btnCancel.setOnClickListener {
            finish()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.viewPager.adapter = MyBookmarkViewPagerAdapter(this@MyBookmarkActivity)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when (position) {
                0 -> tab.text = "매치"
                1 -> tab.text = "용병모집"
                2 -> tab.text = "용병신청"
            }
        }.attach()
    }
}