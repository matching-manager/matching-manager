package com.example.matching_manager.ui.calender

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay

class CalendarViewModel(
) : ViewModel() {
    private val _list: MutableLiveData<List<CalendarModel>> = MutableLiveData() // 뷰모델 내에서만 변경가능
    val list: LiveData<List<CalendarModel>> get() = _list // 읽기전용 리스트

    // 날짜를 선택했을 때 그 날짜의 메모들만 가지고 있는 리스트
    private val _dateList: MutableLiveData<List<CalendarModel>?> = MutableLiveData() // 날짜만 뿌려줄 리스트
    val dateList: MutableLiveData<List<CalendarModel>?> get() = _dateList // 읽기전용 리스트


    fun setCalendarDate(date: CalendarDay) {
        val filterData = list.value?.filter { memoItem ->
            memoItem.day == date.day && memoItem.month == date.month && memoItem.year == date.year
        } //판별

        _dateList.value = filterData
        //전체 메모 데이터인
        //list.value 중에서 파라미터로 받아온 date의 날짜가 동일한 데이터만 받아온다.
        //list/date 를비교
        // _dateList.value = //전체 메모 리스트에서 data와 날짜가 동일한 메모들만 가져온다.
    }

    fun addMemoItem(model: CalendarModel) {
        val currentList = list.value.orEmpty().toMutableList() // 리스트 변경이 가능함
        currentList.add(model)
        _list.value = currentList
    }

    fun removeMemoItem(model: CalendarModel, position: Int) {
        val currentList = list.value.orEmpty().toMutableList() // 리스트 변경이 가능함
        currentList.removeAt(position)
        _list.value = currentList
    }
}