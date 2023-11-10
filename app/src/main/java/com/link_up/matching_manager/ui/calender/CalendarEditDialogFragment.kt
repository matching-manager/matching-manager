package com.link_up.matching_manager.ui.calender

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.link_up.matching_manager.databinding.CalendarEditDialogFragmentBinding
import com.prolificinteractive.materialcalendarview.CalendarDay


class CalendarEditDialogFragment : BottomSheetDialogFragment() {

    private var _binding: CalendarEditDialogFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel : CalendarViewModel by activityViewModels()

    companion object {
        const val EDIT_CALENDAR_MODEL = "edit_calendar_model"
        const val TAG = "CalendarEditDialogFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
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
        val oldModel = arguments?.getParcelable<CalendarModel>(EDIT_CALENDAR_MODEL)

        if (oldModel != null) {
            val memoYear = oldModel.year ?: 0
            val memoMonth = oldModel.month ?: 0
            val memoDay = oldModel.day ?: 0

            // 선택한 날짜에 점을 표시하는 데코레이터 생성
            val selectedDate = CalendarDay.from(memoYear, memoMonth, memoDay)
            val memoDecorator = CalendarMemoDecorator(setOf(selectedDate))

            // 캘린더에 데코레이터 추가
            materialCalendarCalendarEditView.addDecorator(memoDecorator)
            materialCalendarCalendarEditView.setDateSelected(selectedDate, true)

            edtCalendarEditMemo.setText(oldModel.memo)
            edtCalendarEditPlace.setText(oldModel.place)

            btnCalendarEditSave.setOnClickListener {
                val memoText = edtCalendarEditMemo.text.toString()
                val memoPlace = edtCalendarEditPlace.text.toString()
                val date = materialCalendarCalendarEditView.selectedDate

                if (memoText.isNotBlank() && memoPlace.isNotBlank()) {
                    viewModel.editMemoItem(
                        CalendarModel(
                            day=date.day,
                            month = date.month,
                            year = date.year,
                            place = memoPlace,
                            memo = memoText,
                        ),
                        oldModel
                    )
                    dismiss() // 다이얼로그 닫기
                } else {
                    // `memoText`와 `memoPlace` 중 하나라도 입력되지 않았을 때 클릭 비활성화
                    Toast.makeText(
                        requireContext(),
                        "메모, 장소를 꼭 입력하세요",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnCalendarEditCancel.isEnabled = true
                }
            }
        }
        btnCalendarEditCancel.setOnClickListener {
            dismiss() // 다이얼로그 닫기
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

