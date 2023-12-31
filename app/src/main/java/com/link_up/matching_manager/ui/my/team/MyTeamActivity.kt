package com.link_up.matching_manager.ui.my.team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.link_up.matching_manager.databinding.MyTeamActivityBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MyTeamActivity : AppCompatActivity() {
    private lateinit var binding : MyTeamActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyTeamActivityBinding.inflate(layoutInflater)
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

        binding.viewPager.adapter = MyTeamViewPagerAdapter(this@MyTeamActivity)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) {tab, position ->
            when (position) {
                0 -> tab.text = "모집"
                1 -> tab.text = "신청"
            }
        }.attach()
    }
}