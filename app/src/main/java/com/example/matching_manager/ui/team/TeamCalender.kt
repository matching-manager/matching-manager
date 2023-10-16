package com.example.matching_manager.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.databinding.TeamCalenderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TeamCalender : BottomSheetDialogFragment() {

    private var _binding: TeamCalenderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamCalenderBinding.inflate(inflater, container, false)
        initView()
        return binding.root
    }

    private fun initView()= with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}