package com.link_up.matching_manager.ui.calender

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.link_up.matching_manager.databinding.CalendarEditDialogFragmentBinding


class CalendarEditDialogFragment : BottomSheetDialogFragment() {

    private var _binding: CalendarEditDialogFragmentBinding? = null
    private val binding get() = _binding!!


    companion object {
        const val EDIT_REQUEST_KEY = "edit_request_key" // EDIT 다이얼로그 요청 키
        const val EDIT_RESULT_KEY_TEXT = "edit_request_key_text" // EDIT 결과 데이터 키
        const val EDIT_RESULT_KEY_PLACE = "edit_result_key_place" // EDIT 장소 데이터 키
        const val EDIT_RESULT_KEY_SCHEDULE = "edit_result_key_schedule" // EDIT 날짜 데이터 키
        const val EDIT_RESULT_KEY_YEAR = "edit_result_key_year" // EDIT 년 데이터 키
        const val EDIT_RESULT_KEY_MONTH = "edit_result_key_month" // EDIT 월 데이터 키
        const val EDIT_RESULT_KEY_DAY = "edit_result_key_day" // EDIT 일 데이터 키

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
        initView()
    }


    private fun initView() = with(binding) {
        val calendarModel = arguments?.getParcelable<CalendarModel>("calendarModel")

        if (calendarModel != null) {
            val memoText = calendarModel.memo
            val memoPlace = calendarModel.place
            val schedule = "${calendarModel.year}년 ${calendarModel.month}월 ${calendarModel.day}일"

            edtCalendarEditMemo.setText(memoText)
            edtCalendarEditPlace.setText(memoPlace)
            btnCalendarEditSave.setOnClickListener {
                val memoText = edtCalendarEditMemo.text.toString()
                val memoPlace = edtCalendarEditPlace.text.toString()

                val selectedDate = materialCalendarCalendarEditView.selectedDate
                if (selectedDate != null) {
                    val memoYear = binding.materialCalendarCalendarEditView.selectedDate.year
                    val memoMonth = binding.materialCalendarCalendarEditView.selectedDate.month
                    val memoDay = binding.materialCalendarCalendarEditView.selectedDate.day

                    if (memoText.isNotBlank() && memoPlace.isNotBlank()) {
                        // 메모 데이터를 부모 Fragment로 전달합니다.
                        setFragmentResult(
                            EDIT_REQUEST_KEY,
                            bundleOf(
                                EDIT_RESULT_KEY_TEXT to memoText,
                                EDIT_RESULT_KEY_PLACE to memoPlace,
                                EDIT_RESULT_KEY_YEAR to memoYear,
                                EDIT_RESULT_KEY_MONTH to memoMonth,
                                EDIT_RESULT_KEY_DAY to memoDay,
                            )

                        )
                        dismiss() // 다이얼로그 닫기

                    } else {
                        // `memoText`와 `memoPlace` 중 하나라도 입력되지 않았을 때 클릭 비활성화
                        Toast.makeText(requireContext(), "메모, 장소를 꼭 입력하세요", Toast.LENGTH_SHORT)
                            .show()
                        btnCalendarEditCancel.isEnabled = true
                    }
                }
            }

            btnCalendarEditCancel.setOnClickListener {
                dismiss() // 다이얼로그 닫기
            }
        }
    }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }


    }