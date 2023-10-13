package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.MatchItemBinding

class MatchListAdapter(private val onItemClick: (MatchData) -> Unit) :
    ListAdapter<MatchData, MatchListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<MatchData>() {
            override fun areItemsTheSame(oldItem: MatchData, newItem: MatchData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: MatchData, newItem: MatchData): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MatchItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }

    class ViewHolder(private val binding: MatchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MatchData, onItemClick: (MatchData) -> Unit) = with(binding) {
            ivProfile.setImageResource(item.teamImage)
            tvType.text = item.type
            tvDetail.text = item.detail
            tvViewCount.text = item.viewCount.toString()
            tvChatCount.text = item.chatCount.toString()
            tvSchedule.text = item.schedule
            tvPlace.text = item.place

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

}