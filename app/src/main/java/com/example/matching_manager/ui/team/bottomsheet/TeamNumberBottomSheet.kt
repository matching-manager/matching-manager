package com.example.matching_manager.ui.team.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.matching_manager.databinding.TeamNumberBinding
import com.example.matching_manager.ui.team.viewmodel.TeamSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TeamNumberBottomSheet : BottomSheetDialogFragment() {

    private var _binding: TeamNumberBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel :TeamSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamNumberBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNumber()
        initView()
    }


    private fun setNumber()= with(binding) {
        // NumberPicker의 범위 설정 (1명부터 20명까지)
        pickerNumber.minValue = 1
        pickerNumber.maxValue = 15
    }


    private fun initView()= with(binding) {
        btnSave.setOnClickListener {
            var playernumber=pickerNumber.value
            sharedViewModel.updateTeamNumber(playernumber)
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