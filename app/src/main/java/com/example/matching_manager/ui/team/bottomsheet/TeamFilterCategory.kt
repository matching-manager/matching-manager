package com.example.matching_manager.ui.team.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamFilterCategoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TeamFilterCategory : BottomSheetDialogFragment() {

    private var _binding: TeamFilterCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamFilterCategoryBinding.inflate(inflater, container, false)

        setUpSpinner()
        initView()
        return binding.root
    }

    private fun setUpSpinner()= with(binding) {
        // 스피너에 데이터 연결
        val gameAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.game_array,
            android.R.layout.simple_spinner_item
        )
        gameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameSpinner.adapter = gameAdapter


        val areaAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.area_array,
            android.R.layout.simple_spinner_item
        )
        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        citySpinner.adapter = areaAdapter



    }

    private fun initView()= with(binding) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}