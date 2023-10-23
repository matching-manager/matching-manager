package com.example.matching_manager.ui.calender

import CalendarMemoDialogFragment
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
import java.text.SimpleDateFormat
import java.util.Locale


class CalendarFragment : Fragment() {
    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var materialCalendarView: MaterialCalendarView
    private lateinit var calendarRecyclerviewItemBinding: CalendarRecyclerviewItemBinding
    private val memoMap = mutableMapOf<CalendarDay, String>()
    private val viewModel : CalendarViewModel by viewModels() //뷰모델 생성

    private val listAdapter = CalendarListAdapter() { calendarModel ->
        // 아이템 클릭 시의 동작을 정의
        //private val memoList = mutableListOf<Memo>()
        //showMemoDialog(calendarModel)
    }

    companion object {
        fun newInstance() = CalendarFragment()


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalendarFragmentBinding.inflate(inflater, container, false)
        calendarRecyclerviewItemBinding = CalendarRecyclerviewItemBinding.inflate(inflater, container, false)
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

    private fun initViewModel() = with(viewModel){
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

    private fun showMemoDialog(date: CalendarDay) {
        val calendarMemoDialogFragment = CalendarMemoDialogFragment()
        calendarMemoDialogFragment.show(childFragmentManager, calendarMemoDialogFragment.tag)

        // CalendarMemoDialogFragment에 메모를 저장하는 리스너 설정

        childFragmentManager.setFragmentResultListener(CalendarMemoDialogFragment.REQUEST_KEY, viewLifecycleOwner) { requestKey, result ->
            //if (requestKey == CalendarMemoDialogFragment.REQUEST_KEY) {
            val memoText = result.getString(CalendarMemoDialogFragment.RESULT_KEY_TEXT) ?:""

            val clickedDay = date.day.toString() // 클릭한 날짜의 일 정보
            //val clickedMonth = (date.month + 1).toString() // 클릭한 날짜의 월 정보

            val simpleDateFormat = SimpleDateFormat("MMM", Locale.US)
            val clickedMonth = simpleDateFormat.format(date.date)

            Log.d("getmemoText", "MemoText: $memoText")

            // memoText를 이용해 원하는 처리를 수행합니다.
            val calendarModel = CalendarModel(
                clickedDay,
                clickedMonth,
                "운동장", // 여기서 월을 원하는 형식으로 변경
                memoText
            )


            memoMap[date] = memoText
            viewModel.addMemoItem(calendarModel)


            if (memoText != null && memoText.isNotEmpty()) {
                val datesWithMemo = memoMap.keys.toSet()
                val memoDecorator = MemoDecorator(datesWithMemo)
                materialCalendarView.addDecorator(memoDecorator)
            }
         }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}