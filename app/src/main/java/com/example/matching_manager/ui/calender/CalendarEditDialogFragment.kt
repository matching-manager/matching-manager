package com.example.matching_manager.ui.calender

import CalendarAddDialogFragment.Companion.ADD_REQUEST_KEY
import CalendarAddDialogFragment.Companion.ADD_RESULT_KEY_PLACE
import CalendarAddDialogFragment.Companion.ADD_RESULT_KEY_TEXT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.matching_manager.databinding.CalendarEditDialogFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CalendarEditDialogFragment : BottomSheetDialogFragment() {

    private var _binding: CalendarEditDialogFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val EDIT_REQUEST_KEY = "edit_request_key" // EDIT 다이얼로그 요청 키
        const val EDIT_RESULT_KEY_TEXT = "edit_reqest_key_text" // EDIT 결과 데이터 키
        const val EDIT_RESULT_KEY_PLACE = "edit_result_key_place" // EDIT 장소 데이터 키
        const val EDIT_RESULT_KEY_SCHEDULE = "edit_result_key_schedule" // EDIT 날짜 데이터 키

        fun newInstance(): CalendarEditDialogFragment {
            return CalendarEditDialogFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarEditDialogFragmentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarModel = arguments?.getParcelable<CalendarModel>("calendarModel")

        if (calendarModel != null) {
            val memoText = calendarModel.memo
            val place = calendarModel.place
            val schedule = "${calendarModel.year}년 ${calendarModel.month}월 ${calendarModel.day}일"

            binding.edtCalendarEditMemo.setText(memoText)
            binding.edtCalendarEditPlace.setText(place)
            binding.edtCalendarEditSchedule.setText(schedule)
        }

        binding.btnCalendarEditSave.setOnClickListener {

            val memoedit_text = binding.edtCalendarEditMemo.text.toString()
            val memoedit_place = binding.edtCalendarEditPlace.text.toString()

            if (memoedit_text.isNotBlank() && memoedit_place.isNotBlank()) {
                // 메모 데이터를 부모 Fragment로 전달합니다.
                setFragmentResult(
                    EDIT_REQUEST_KEY,
                    bundleOf(EDIT_RESULT_KEY_TEXT to memoedit_text, EDIT_RESULT_KEY_PLACE to memoedit_place)
                )
                dismiss() // 다이얼로그 닫기
            } else {
                // `memoText`와 `memoPlace` 중 하나라도 입력되지 않았을 때 클릭 비활성화
                Toast.makeText(requireContext(), "메모, 장소를 꼭 입력하세요", Toast.LENGTH_SHORT).show()
                binding.btnCalendarEditCancel.isEnabled = true
            }
        }
        binding.btnCalendarEditCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}