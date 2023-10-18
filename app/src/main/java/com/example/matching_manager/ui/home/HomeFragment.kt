package com.example.matching_manager.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.R
import com.example.matching_manager.databinding.HomeFragmentBinding
import com.example.matching_manager.databinding.SignInFragmentBinding
import com.example.matching_manager.ui.calender.CalendarFragment
import com.example.matching_manager.ui.home.alarm.AlarmActivity
import com.example.matching_manager.ui.home.arena.ArenaActivity
import com.example.matching_manager.ui.home.arena.ArenaViewModel

class HomeFragment : Fragment() {
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() = with(binding) {
        btnArena.setOnClickListener {
            val intent = Intent(requireContext(), ArenaActivity::class.java)
            startActivity(intent)
        }
        btnHomeNoti.setOnClickListener {
            val intent = Intent(requireContext(), AlarmActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}