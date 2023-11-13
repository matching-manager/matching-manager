package com.link_up.matching_manager.ui.calender

import CalendarAddDialogFragment
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.CalendarFragmentBinding
import com.link_up.matching_manager.databinding.CalendarRecyclerviewItemBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView


class CalendarFragment : Fragment() {
    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var materialCalendarView: MaterialCalendarView
    private lateinit var calendarRecyclerviewItemBinding: CalendarRecyclerviewItemBinding
    private val viewModel: CalendarViewModel by activityViewModels {
        CalendarViewModelFactory(
            requireContext()
        )
    } //뷰모델 생성

    private val listAdapter = CalendarListAdapter(
        onCalendarItemClick = { calendarModel, position ->
            showEditMemoDialog(calendarModel, position)
        },
        onCalendarItemLongClick = { calendarModel, position ->
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

        binding.calendarFabAdd.setOnClickListener {
            showAddMemoDialog()
            //showEditMemoDialog()
        }
    }


    private fun initViewModel() = with(viewModel) {
        loadCalendarData()

        list.observe(viewLifecycleOwner, Observer { // 전체 리스트를 관찰해 span관리
            updateSpan(it)
            saveCalendarData()
        })

        dateList.observe(viewLifecycleOwner, Observer { // 리스트 관찰
            listAdapter.submitList(it)
        })
    }

    private fun initView() = with(binding) {
        toolBar.title = R.string.toolbar_calendar.toString()
        materialCalendarView = materialCalendar // 초기화 코드를 추가
        //리사이클러뷰 어댑터 설정
        rvCalendar.adapter = listAdapter

        materialCalendar.setOnDateChangedListener() { widget, date, selected ->
            viewModel.setCalendarDate(date)
        }
    }

    private fun showDeleteConfirmationDialog(calendarModel: CalendarModel, position: Int) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setTitle("일정 삭제")
        alertDialogBuilder.setMessage("이 일정을 삭제하시겠습니까?")

        alertDialogBuilder.setPositiveButton("삭제") { _, _ ->
            viewModel.removeMemoItem(calendarModel) // 삭제 동작을 수행합니다.
        }
        alertDialogBuilder.setNegativeButton("취소") { _, _ ->
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun showAddMemoDialog() {
        val calendarAddMemoDialogFragment = CalendarAddDialogFragment()
        calendarAddMemoDialogFragment.show(childFragmentManager, calendarAddMemoDialogFragment.tag)
        // CalendarMemoDialogFragment에 메모를 저장하는 리스너 설정
        childFragmentManager.setFragmentResultListener(
            CalendarAddDialogFragment.ADD_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, result ->
            if (!result.getString(CalendarAddDialogFragment.ADD_RESULT_KEY_TEXT).isNullOrEmpty()) {
                val memoText = result.getString(CalendarAddDialogFragment.ADD_RESULT_KEY_TEXT)
                val memoPlace =
                    result.getString(CalendarAddDialogFragment.ADD_RESULT_KEY_PLACE) ?: ""

                val calendarDay = result.getInt("add_result_key_day", 0)
                val calendarMonth = result.getInt("add_result_key_month", 0)
                val calendarYear = result.getInt("add_result_key_year", 0)

                Log.d("getmemoText", "MemoText: $memoText")

                val calendarModel = CalendarModel(
                    calendarDay,
                    calendarMonth,
                    calendarYear,
                    memoPlace,
                    memoText, // memoText를 이용해 원하는 처리를 수행합니다.
                )

                viewModel.addMemoItem(calendarModel)
            }
        }
    }

    private fun showEditMemoDialog(calendarModel: CalendarModel, position: Int) {
        // 1. CalendarEditDialogFragment의 인스턴스 생성
        val editDialogFragment = CalendarEditDialogFragment()
        // 2. 필요한 데이터를 Bundle에 추가하여 전달
        val bundle = Bundle()
        bundle.apply {
            putParcelable(CalendarEditDialogFragment.EDIT_CALENDAR_MODEL, calendarModel)
        }
        editDialogFragment.arguments = bundle
        editDialogFragment.show(parentFragmentManager, editDialogFragment.tag)
    }


    private fun updateSpan(calendarModels: List<CalendarModel>) = with(binding) {
        // 모든 Span 제거
        materialCalendarView.removeDecorators()

        // memoMap에서 메모가 있는 모든 날짜를 가져와서 Set으로 만듦
        for (model in calendarModels) {
            val memoDecorator = CalendarMemoDecorator(
                setOf(
                    CalendarDay.from(
                        model.year ?: 0,
                        model.month ?: 0,
                        model.day ?: 0
                    )
                )
            )
            materialCalendarView.addDecorator(memoDecorator)
        }
        materialCalendar.selectedDate?.let {
            viewModel.setCalendarDate(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}