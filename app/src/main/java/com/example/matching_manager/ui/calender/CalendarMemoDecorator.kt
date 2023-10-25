package com.example.matching_manager.ui.calender

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CalendarMemoDecorator(private val datesWithMemo: Set<CalendarDay>) : DayViewDecorator {
    


    override fun shouldDecorate(day: CalendarDay?): Boolean {

        return datesWithMemo.contains(day)  // 표식을 추가할 날짜가 있는 경우 true 반환
        //return datesWithMemo.contains(dateString)
    }


    override fun decorate(view: DayViewFacade) {
        if (datesWithMemo.isNotEmpty()) {
            view.addSpan(DotSpan(10F, Color.TRANSPARENT))
        } else
            view.addSpan(DotSpan(15F, Color.TRANSPARENT))
    }

    fun clearSpans(){
        //spans.clear()
    }

}

