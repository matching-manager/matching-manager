package com.example.matching_manager.ui.calender

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.CalendarRecyclerviewItemBinding

class CalendarListAdapter(
    private val onCalendarItemClick: (CalendarModel) -> Unit
) : ListAdapter<CalendarModel, CalendarListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<CalendarModel>() {
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


    class ViewHolder(
        private val binding: CalendarRecyclerviewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: CalendarModel,
        ) = with(binding) {

            tvSchedulePlace.text = item.place
            //tvScheduleDay.text = it.day
            //tvScheduleMonth.text = it.month
            //tvScheduleDivision.text = item.division

            tvScheduleDay.text = item.day
            tvScheduleMonth.text = item.month
            tvScheduleMemo.text = item.memo
            //item이라고 만든 이유

        }

    }


}

