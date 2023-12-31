package com.link_up.matching_manager.ui.match.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.link_up.matching_manager.databinding.MatchTimeBinding
import com.link_up.matching_manager.ui.match.MatchSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MatchTime : BottomSheetDialogFragment() {

    private var _binding: MatchTimeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MatchSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MatchTimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }
    private fun initView() = with(binding) {
        btnSave.setOnClickListener {
            val hour = pickerTime.hour
            val minute = pickerTime.minute
            val amPm = if (hour < 12) "오전" else "오후"

            sharedViewModel.updateTeamTime(hour, minute, amPm)
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