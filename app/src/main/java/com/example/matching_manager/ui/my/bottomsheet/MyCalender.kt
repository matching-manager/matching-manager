package com.example.matching_manager.ui.my.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.matching_manager.databinding.MatchCalenderBinding
import com.example.matching_manager.databinding.MyCalenderBinding
import com.example.matching_manager.ui.match.MatchSharedViewModel
import com.example.matching_manager.ui.my.MySharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar


class MyCalender : BottomSheetDialogFragment() {

    private var _binding: MyCalenderBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MySharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = MyCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        // 캘린더 날짜가 선택될 때의 리스너를 등록합니다.
        btnSelect.setOnClickListener {
            val calendar = Calendar.getInstance()

            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1 // 월은 0부터 시작하므로 +1 해줍니다.
            val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
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


            sharedViewModel.updateCalendar(year, month, dayOfMonth, dayOfWeekString)

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