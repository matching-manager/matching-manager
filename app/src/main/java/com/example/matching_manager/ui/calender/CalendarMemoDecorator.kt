package com.example.matching_manager.ui.calender

import android.graphics.Color
import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

class CalendarMemoDecorator(private val datesWithMemo: Set<CalendarDay>) : DayViewDecorator {

    private var spans = mutableListOf<DotSpan>()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return datesWithMemo.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        if (datesWithMemo.isNotEmpty()) {
            view.addSpan(DotSpan(20F, Color.BLUE))
        } else {
            view.addSpan(DotSpan(20F, Color.TRANSPARENT))
        }

    }
}


