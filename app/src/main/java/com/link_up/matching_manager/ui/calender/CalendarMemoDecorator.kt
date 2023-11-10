package com.link_up.matching_manager.ui.calender

import android.graphics.Color
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


class CalendarMemoDecorator(private val datesWithMemo: Set<CalendarDay>) : DayViewDecorator {

    private var spans = mutableListOf<DotSpan>()

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return datesWithMemo.contains(day)
    }

    //val calendarDeepGreen = ContextCompat.getColor(context, R.color.calendar_deep_green)
    //val green : Int = 0x219653

    override fun decorate(view: DayViewFacade) {
        if (datesWithMemo.isNotEmpty()) {
            view.addSpan(DotSpan(10F, Color.parseColor("#FF219653")))
        } else {
                view.addSpan(DotSpan(0F, Color.parseColor("#FFFFFF")))
            }
        }
    }



