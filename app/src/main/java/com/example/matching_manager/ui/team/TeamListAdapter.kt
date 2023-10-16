package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.TeamItemBinding
import com.example.matching_manager.ui.team.mdoel.TeamModel

class TeamListAdapter(private val onItemClick: (TeamModel) -> Unit) :
    ListAdapter<TeamModel, TeamListAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<TeamModel>() {
            override fun areItemsTheSame(oldItem: TeamModel, newItem: TeamModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: TeamModel, newItem: TeamModel): Boolean {
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

        fun bind(item: TeamModel, onItemClick: (TeamModel) -> Unit) = with(binding) {
            ivProfile.setImageResource(item.teamProfile)
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