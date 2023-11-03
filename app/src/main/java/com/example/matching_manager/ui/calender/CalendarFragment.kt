package com.example.matching_manager.ui.calender

import CalendarAddDialogFragment
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.matching_manager.databinding.CalendarFragmentBinding
import com.example.matching_manager.databinding.CalendarRecyclerviewItemBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.time.Year
import java.util.Calendar


class CalendarFragment : Fragment() {
    private var _binding: CalendarFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var materialCalendarView: MaterialCalendarView
    private lateinit var calendarRecyclerviewItemBinding: CalendarRecyclerviewItemBinding
    private val memoMap = mutableMapOf<CalendarDay, String>()
    private val viewModel: CalendarViewModel by viewModels() //뷰모델 생성
    private lateinit var calendarEditDialogFragment: CalendarEditDialogFragment
    private val openCalendarDetail =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            onActivityResult(1, result.resultCode, result.data)
        }

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

        listAdapter.onCalendarItemClick = { calendarModel ->

            // 1. CalendarEditDialogFragment의 인스턴스 생성
            val editDialogFragment = CalendarEditDialogFragment()

            // 2. 필요한 데이터를 Bundle에 추가하여 전달
            val bundle = Bundle()
            bundle.putParcelable("calendarModel", calendarModel)
            parentFragmentManager.setFragmentResult("request_key", bundle)
            editDialogFragment.arguments = bundle
            editDialogFragment.show(parentFragmentManager, editDialogFragment.tag)

        }
        // setFragmentResultListener 설정
        parentFragmentManager.setFragmentResultListener(
            CalendarEditDialogFragment.EDIT_REQUEST_KEY,
            viewLifecycleOwner
        ) { request_edit_Key, edit_result ->
            if (request_edit_Key == CalendarEditDialogFragment.EDIT_REQUEST_KEY) {
                val editMemoText =
                    edit_result.getString(CalendarEditDialogFragment.EDIT_RESULT_KEY_TEXT)
                val editMemoPlace =
                    edit_result.getString(CalendarEditDialogFragment.EDIT_RESULT_KEY_PLACE)

            }
        }

        binding.calendarFabAdd.setOnClickListener {
            showMemoDialog()

        }
    }


    private fun initViewModel() = with(viewModel) {

        list.observe(viewLifecycleOwner, Observer { // 리스트 관찰
            listAdapter.submitList(it)
        })

        dateList.observe(viewLifecycleOwner, Observer { // 리스트 관찰
            listAdapter.submitList(it)
        })


    }

    private fun initView() = with(binding) {

        //리사이클러뷰 어댑터 설정
        rvCalendar.adapter = listAdapter


        materialCalendar.setOnDateChangedListener() { widget, date, selected ->

            viewModel.setCalendarDate(date)
            Toast.makeText(requireContext(), "${date}", Toast.LENGTH_SHORT).show()


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
                                it2,
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
            materialCalendarView.invalidateDecorators()
            materialCalendarView.addDecorator(memoDecorator)

        }

        alertDialogBuilder.setNegativeButton("취소") { _, _ ->
            // 취소 동작을 수행합니다. (아무 동작 필요 없을 때)
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val updatedMemoText = data?.getStringExtra("updatedMemoText")
            if (updatedMemoText != null) {
                // 업데이트된 메모 텍스트를 사용하여 UI를 업데이트합니다.
                // 예를 들어, 해당 아이템을 찾아 업데이트된 메모 텍스트를 설정하고 RecyclerView를 갱신할 수 있습니다.
            }
        }
    }

    private fun showMemoDialog() {
        val calendarAddMemoDialogFragment = CalendarAddDialogFragment()
        calendarAddMemoDialogFragment.show(childFragmentManager, calendarAddMemoDialogFragment.tag)

        // CalendarMemoDialogFragment에 메모를 저장하는 리스너 설정

        childFragmentManager.setFragmentResultListener(
            CalendarAddDialogFragment.ADD_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, result ->
            //if (requestKey == CalendarMemoDialogFragment.REQUEST_KEY) {
            val memoText = result.getString(CalendarAddDialogFragment.ADD_RESULT_KEY_TEXT) ?: ""
            // CalendarMemoDialogFragment에서 전달된 메모 텍스트를 가져옵니다. 만약 값이 null인 경우 빈 문자열로 초기화합니다.

            val memoPlace = result.getString(CalendarAddDialogFragment.ADD_RESULT_KEY_PLACE) ?: ""

            val calendarDay = result.getInt("add_result_key_day", 0)
            val calendarMonth = result.getInt("add_result_key_month", 0)
            val calendarYear = result.getInt("add_result_key_year", 0)

            //val memoSchedule = date.year + date.month + date.day // 클릭한 날짜의 년월일 정보
            val memoSchedule =
                result.getString(CalendarAddDialogFragment.ADD_RESULT_KEY_SCHEDULE)
                    ?: Calendar.DATE.toString()

            Log.d("getmemoText", "MemoText: $memoText")
            //Log.d("getplaceText", "MemoText: $memoPlace")

            val calendarModel = CalendarModel(
                calendarDay,
                calendarMonth,
                calendarYear,
                memoPlace,
                memoText, // memoText를 이용해 원하는 처리를 수행합니다.
                memoSchedule,
            )

            viewModel.addMemoItem(calendarModel)
            memoMap[CalendarDay(calendarYear, calendarMonth, calendarDay)] = memoText

            if (memoText != null && memoText.isNotEmpty()) { // 만약 memoText가 null이 아니고 비어있지 않다면, 메모 텍스트가 존재한다는 것입니다.
                val datesWithMemo = memoMap.keys.toSet()  // memoMap에서 메모가 있는 날짜들의 집합을 가져옵니다.
                val memoDecorator =
                    CalendarMemoDecorator(datesWithMemo) // CalendarMemoDecorator를 생성하고, 해당 날짜들에 점을 추가하는 역할을 합니다.
              //materialCalendarView.addDecorator(memoDecorator)// MaterialCalendarView에 memoDecorator를 추가하여, 메모가 있는 날짜에 점이 찍히도록 합니다.
            } else {
                val memoDecorator =
                    CalendarMemoDecorator(emptySet()) // 데코레이터를 빈 집합으로 초기화하여 아무 점도 표시되지 않도록 합니다.
                materialCalendarView.removeDecorator(memoDecorator) // 데코레이터를 제거합니다.
                materialCalendarView.invalidateDecorators() // 데코레이터를 다시 그립니다.
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}