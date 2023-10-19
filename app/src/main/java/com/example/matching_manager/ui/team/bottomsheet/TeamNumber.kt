package com.example.matching_manager.ui.team.bottomsheet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.matching_manager.databinding.TeamCalenderBinding
import com.example.matching_manager.databinding.TeamNumberBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TeamNumber : BottomSheetDialogFragment() {

    private var _binding: TeamNumberBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamNumberBinding.inflate(inflater, container, false)
        setNumber()
        initView()
        return binding.root
    }

    private fun setNumber()= with(binding) {
        // NumberPicker의 범위 설정 (1명부터 20명까지)
        pickerNumber.minValue = 0
        pickerNumber.maxValue = 20
    }

    private fun initView()= with(binding) {
        btnSave.setOnClickListener {
            //선택 동작 추가하기
            dismiss() // BottomSheet 닫기
        }
        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}