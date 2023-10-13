package com.example.matching_manager.ui.team

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamFilterCategoryBinding
import com.example.matching_manager.ui.team.mdoel.GameModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TeamFilterCategory : BottomSheetDialogFragment() {

    private var _binding: TeamFilterCategoryBinding? = null
    private val binding get() = _binding!!
    private val gameSpinnerList = ArrayList<GameModel>()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamFilterCategoryBinding.inflate(inflater, container, false)

        return binding.root
        initView()
    }

    private fun initView()= with(binding) {
    // 농구, 축구, 풋살을 추가
        gameSpinnerList.add(GameModel("풋살"))
        gameSpinnerList.add(GameModel("축구"))
        gameSpinnerList.add(GameModel("농구"))
        gameSpinnerList.add(GameModel("배드민턴"))
        gameSpinnerList.add(GameModel("볼링"))

        val arraySpinnerUpper = gameSpinnerList.map { it.gametitle }.toTypedArray()

        // 스피너에 데이터 연결
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            arraySpinnerUpper
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gameSpinner.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}