package com.example.matching_manager.ui.home.arena.alarm

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.AlarmRecyclerviewItemBinding


class AlarmListAdapter(
    private val onCallClick: (AlarmModel) -> Unit
) : ListAdapter<AlarmModel, AlarmListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<AlarmModel>() {
        override fun areItemsTheSame(
            oldItem: AlarmModel,
            newItem: AlarmModel
        ): Boolean {
            return oldItem.userId == newItem.userId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AlarmModel,
            newItem: AlarmModel
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
            AlarmRecyclerviewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onCallClick
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = getItem(position) // ListAdapter의 메소드 getItem
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: AlarmRecyclerviewItemBinding,
        private val onCallClick: (AlarmModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AlarmModel) = with(binding) {
            item.let {
                tvTitle.text = it.body
                tvUser.text = it.userId
                tvPhoneNumber.text = it.phoneNumber
            }
            btnCall.setOnClickListener {
                onCallClick(item)
            }
        }
    }
}