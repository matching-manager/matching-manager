package com.example.matching_manager.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.matching_manager.databinding.TeamFragmentBinding
import com.example.matching_manager.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

class TeamFragment : Fragment() {
    private var _binding: TeamFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewPagerAdapter by lazy { TeamViewPagerAdapter(requireActivity() as FragmentActivity) }

    companion object {
        fun newInstance() = TeamFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TeamFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        //view pager adapter
        viewPager.adapter = viewPagerAdapter
        //viewpager slide
        viewPager.isUserInputEnabled=false

        // TabLayout x ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}