package com.link_up.matching_manager.ui.my.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.link_up.matching_manager.databinding.MyNumberBinding
import com.link_up.matching_manager.ui.my.my.MySharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class MyNumberBottomSheet : BottomSheetDialogFragment() {

    private var _binding: MyNumberBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MySharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MyNumberBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNumber()
        initView()
    }


    private fun setNumber()= with(binding) {
        // NumberPicker의 범위 설정 (1명부터 20명까지)
        pickerNumber.minValue = 0
        pickerNumber.maxValue = 20
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