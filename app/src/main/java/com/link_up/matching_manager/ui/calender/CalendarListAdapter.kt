package com.link_up.matching_manager.ui.calender

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.link_up.matching_manager.databinding.CalendarRecyclerviewItemBinding
import java.util.Calendar
import java.util.Locale

class CalendarListAdapter(
    public var onCalendarItemClick: (CalendarModel) -> Unit,
    private val onCalendarItemLongClick: (CalendarModel, Int) -> Unit
) : ListAdapter<CalendarModel, CalendarListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CalendarModel>() { // DiffUtil.ItemCallback을 사용하여 아이템 변경 감지
        override fun areItemsTheSame(
            oldItem: CalendarModel,
            newItem: CalendarModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: CalendarModel,
            newItem: CalendarModel
        ): Boolean {
            return oldItem == newItem
        }


    }
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            CalendarRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCalendarItemClick,
            onCalendarItemLongClick
            //onCalendarItemClick
        )

    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position)
        holder.bind(item)
    }

    fun onLongClick(position: Int) {
        val item = getItem(position)
        onCalendarItemLongClick(item, position)
    }

    class ViewHolder(
        private val binding: CalendarRecyclerviewItemBinding,
        private val onCalendarItemClick: (CalendarModel) -> Unit,
        private val onCalendarItemLongClick: (CalendarModel, Int) -> Unit // 긴 클릭 처리를 위한 콜백 함수 추가
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CalendarModel,
        ) = with(binding) {

            tvSchedulePlace.text = item.place
            //tvScheduleDay.text = it.day
            //tvScheduleMonth.text = it.month
            //tvScheduleDivision.text = item.division

            tvScheduleDay.text = item.day.toString()

            val simpleDataFormat = SimpleDateFormat("MMM", Locale.ENGLISH)
            val monthName = simpleDataFormat.format(Calendar.getInstance().apply {
                set(Calendar.MONTH, item.month!!) // Calendar.MONTH는 0부터 시작하기 때문에 1을 뺍니다.
            }.time)

            tvScheduleMonth.text = monthName
            tvScheduleYear.text = item.year.toString()
            tvScheduleMemo.text = item.memo
            tvSchedule.text = "${item.year}년 ${item.month}월 ${item.day}일"

            //item이라고 만든 이유

            itemView.setOnClickListener {
                onCalendarItemClick(item)
            }

            // 아이템을 길게 클릭할 때 처리
            itemView.setOnLongClickListener() {
                onCalendarItemLongClick(item, adapterPosition)
                true
            }

        }

    }

}

