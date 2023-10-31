package com.example.matching_manager.ui.calender

import CalendarMemoDialogFragment
import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.CalendarFragmentBinding
import com.example.matching_manager.databinding.CalendarRecyclerviewItemBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView


class CalendarFragment : Fragment() {
    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var materialCalendarView: MaterialCalendarView
    private lateinit var calendarRecyclerviewItemBinding: CalendarRecyclerviewItemBinding
    private val memoMap = mutableMapOf<CalendarDay, String>()
    private val viewModel: CalendarViewModel by viewModels() //뷰모델 생성

    private val listAdapter = CalendarListAdapter(
        onCalendarItemClick = { calendarModel ->  /* 항목 클릭 시 동작 */ },

        onCalendarItemLongClick = { calendarModel, position
            ->
            showDeleteConfirmationDialog(calendarModel, position)
        }
    )


    companion object {
        fun newInstance() = CalendarFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarFragmentBinding.inflate(inflater, container, false)
        calendarRecyclerviewItemBinding =
            CalendarRecyclerviewItemBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()


//        val CalendarDataList = listOf(
//            CalendarModel("1", "NOV", "Place A", "Division X", ""),
//            CalendarModel("15", "NOV", "Place B", "Division Y", ""),
//            CalendarModel("28", "NOV", "Place C", "Division Z", ""),
//            CalendarModel("28", "7", "Place C", "Division A", ""),
//            CalendarModel("28", "7", "Place C", "Division B", "")
//        )


        //val tvScheduleMemo= calendarRecyclerviewItemBinding.tvScheduleMemo

        //val tvScheduleMemo = binding.tvScheduleMemo
        //val memoTextLiveData = memoTextLiveData

    }

    private fun initViewModel() = with(viewModel) {
        list.observe(viewLifecycleOwner, Observer { // 리스트 관찰
            listAdapter.submitList(it)

        })

    }

    private fun initView() = with(binding) {

        //리사이클러뷰 어댑터 설정
        rvCalendar.adapter = listAdapter
        //rvCalendar.layoutManager = LinearLayoutManager(requireContext())

        materialCalendarView = binding.materialCalendar
        materialCalendarView.setOnDateChangedListener() { widget, date, selected ->

            showMemoDialog(date)
        }

    }

    private fun showDeleteConfirmationDialog(calendarModel: CalendarModel, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("일정 삭제")
        alertDialogBuilder.setMessage("이 일정을 삭제하시겠습니까?")

        alertDialogBuilder.setPositiveButton("삭제") { _, _ ->
            viewModel.removeMemoItem(calendarModel, position) // 삭제 동작을 수행합니다.

            val deletedDate =
                calendarModel.year?.let { it ->
                calendarModel.month?.let { it1 ->
                    calendarModel.day?.let { it2 ->
                        CalendarDay.from(
                            it,
                            it1,
                            it2
                        )
                    }
                }
            }



            if (deletedDate != null) {
                memoMap.remove(deletedDate)
            }

            val datesWithMemo = memoMap.keys.toSet()
            val memoDecorator = CalendarMemoDecorator(datesWithMemo)

            materialCalendarView.removeDecorator(memoDecorator)
            //materialCalendarView.invalidateDecorators()
            //materialCalendarView.addDecorator(memoDecorator)

    }

        alertDialogBuilder.setNegativeButton("취소") { _, _ ->
            // 취소 동작을 수행합니다. (아무 동작 필요 없을 때)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showMemoDialog(date: CalendarDay) {
        val calendarMemoDialogFragment = CalendarMemoDialogFragment()
        calendarMemoDialogFragment.show(childFragmentManager, calendarMemoDialogFragment.tag)

        // CalendarMemoDialogFragment에 메모를 저장하는 리스너 설정

        childFragmentManager.setFragmentResultListener(
            CalendarMemoDialogFragment.REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, result ->
            //if (requestKey == CalendarMemoDialogFragment.REQUEST_KEY) {
            val memoText = result.getString(CalendarMemoDialogFragment.RESULT_KEY_TEXT) ?: ""
            // CalendarMemoDialogFragment에서 전달된 메모 텍스트를 가져옵니다. 만약 값이 null인 경우 빈 문자열로 초기화합니다.

            val memoPlace = result.getString(CalendarMemoDialogFragment.RESULT_KEY_PLACE) ?: ""

            val clickedDay = date.day // 클릭한 날짜의 일 정보

            val clickedMonth = date.month // Calendar.MONTH는 0부터 시작하기 때문에 1을 뺍니다.

            //val clickedMonth = (date.month + 1) .toString()// 클릭한 날짜의 월정보
            val clickedYear = date.year // 클릭한 날짜의 년 정보

            Log.d("getmemoText", "MemoText: $memoText")
            //Log.d("getplaceText", "MemoText: $memoPlace")

            val calendarModel = CalendarModel(
                clickedDay,
                clickedMonth,
                clickedYear,
                memoPlace,
                memoText, // memoText를 이용해 원하는 처리를 수행합니다.
            )

            viewModel.addMemoItem(calendarModel)
            memoMap[date] = memoText

            if (memoText != null && memoText.isNotEmpty()) { // 만약 memoText가 null이 아니고 비어있지 않다면, 메모 텍스트가 존재한다는 것입니다.
                val datesWithMemo = memoMap.keys.toSet()  // memoMap에서 메모가 있는 날짜들의 집합을 가져옵니다.
                val memoDecorator =
                    CalendarMemoDecorator(datesWithMemo) // CalendarMemoDecorator를 생성하고, 해당 날짜들에 점을 추가하는 역할을 합니다.
                materialCalendarView.addDecorator(memoDecorator) // MaterialCalendarView에 memoDecorator를 추가하여, 메모가 있는 날짜에 점이 찍히도록 합니다.

            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}