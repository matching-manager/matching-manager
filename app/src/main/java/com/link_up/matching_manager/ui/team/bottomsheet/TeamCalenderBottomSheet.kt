package com.link_up.matching_manager.ui.team.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.link_up.matching_manager.databinding.TeamCalenderBinding
import com.link_up.matching_manager.ui.team.view_model.TeamSharedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar


class TeamCalenderBottomSheet : BottomSheetDialogFragment() {

    private var _binding: TeamCalenderBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: TeamSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = TeamCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        // 캘린더 날짜가 선택될 때의 리스너를 등록합니다.
        materialCalendar.setOnDateChangeListener { view, year, month, dayOfMonth ->

            val calendar = Calendar.getInstance()
            val today = Calendar.getInstance()

            calendar.set(year, month, dayOfMonth) // 선택된 날짜로 설정

            // 만약 선택한 날짜가 오늘보다 이전인 경우
            if (calendar.before(today)) {
                // 토스트 메시지로 안내를 띄웁니다.
                Toast.makeText(requireContext(), "과거의 날짜는 선택할 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnDateChangeListener // 날짜 선택을 무시하고 함수를 종료합니다.
            }

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
                sharedViewModel.updateCalendar(year, month + 1, dayOfMonth, dayOfWeekString)
                dismiss() // BottomSheet 닫기
            }
        }
        btnSelect.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
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
                else -> "알 수 없음"
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