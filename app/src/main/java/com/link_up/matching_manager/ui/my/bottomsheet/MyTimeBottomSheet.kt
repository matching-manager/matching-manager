package com.link_up.matching_manager.ui.my.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.link_up.matching_manager.databinding.MyTimeBinding
import com.link_up.matching_manager.ui.my.my.MySharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MyTimeBottomSheet : BottomSheetDialogFragment() {

    private var _binding: MyTimeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MySharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MyTimeBinding.inflate(inflater, container, false)
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