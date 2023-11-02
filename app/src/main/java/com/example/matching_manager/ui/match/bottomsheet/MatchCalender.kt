package com.example.matching_manager.ui.match.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.matching_manager.databinding.MatchCalenderBinding
import com.example.matching_manager.ui.match.MatchSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar


class MatchCalender : BottomSheetDialogFragment() {

    private var _binding: MatchCalenderBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MatchSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MatchCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        // 캘린더 날짜가 선택될 때의 리스너를 등록합니다.
        // 캘린더 날짜가 선택될 때의 리스너를 등록합니다.
        materialCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            val dayOfWeekString = when (dayOfWeek) {
                Calendar.SUNDAY -> "(일)"
                Calendar.MONDAY -> "(월)"
                Calendar.TUESDAY -> "(화)"
                Calendar.WEDNESDAY -> "(수)"
                Calendar.THURSDAY -> "(목)"
                Calendar.FRIDAY -> "(금)"
                Calendar.SATURDAY -> "(토)"
                else -> ""
            }
            btnSelect.setOnClickListener {
                sharedViewModel.updateCalendar(year, month+ 1, dayOfMonth, dayOfWeekString)
                dismiss() // BottomSheet 닫기
            }

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