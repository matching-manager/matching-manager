package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.TeamItemBinding
import com.example.matching_manager.ui.team.TeamItem

class TeamListAdapter(private val onItemClick: (TeamItem) -> Unit) :
    ListAdapter<TeamItem, TeamListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<TeamItem>() {
            override fun areItemsTheSame(oldItem: TeamItem, newItem: TeamItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TeamItem, newItem: TeamItem): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TeamItemBinding.inflate(
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

    class ViewHolder(private val binding: TeamItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeamItem, onItemClick: (TeamItem) -> Unit) = with(binding) {
//            ivProfile.setImageResource(item.teamProfile)
//            tvType.text = item.type
//            tvDetail.text = item.detail
//            tvViewCount.text = item.viewCount.toString()
//            tvChatCount.text = item.chatCount.toString()
//            tvSchedule.text = item.schedule
//            tvPlace.text = item.place

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

}