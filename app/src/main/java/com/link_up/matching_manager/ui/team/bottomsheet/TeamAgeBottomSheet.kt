package com.link_up.matching_manager.ui.team.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.link_up.matching_manager.databinding.TeamAgeBinding
import com.link_up.matching_manager.ui.team.view_model.TeamSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class TeamAgeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: TeamAgeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: TeamSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamAgeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNumber()
        initView()
    }

    private fun setNumber() = with(binding) {
        // NumberPicker의 범위 설정 (1명부터 20명까지)
        pickerAge.minValue = 8
        pickerAge.maxValue = 100
    }

    private fun initView() = with(binding) {
        btnSave.setOnClickListener {
            var age = pickerAge.value
            sharedViewModel.updateTeamAge(age)
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