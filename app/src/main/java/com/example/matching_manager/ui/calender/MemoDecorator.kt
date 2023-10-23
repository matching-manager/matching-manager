package com.example.matching_manager.ui.calender

import android.graphics.Color
import com.google.firebase.database.collection.LLRBNode
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class MemoDecorator(private val datesWithMemo: Set<CalendarDay>) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        // 표식을 추가할 날짜가 있는 경우 true 반환
        return datesWithMemo.contains(day)
    }

    override fun decorate(view: DayViewFacade) {

        view.addSpan(DotSpan(10F, Color.BLUE))// 표식 추가
    }
}
