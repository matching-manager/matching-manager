package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.MatchItemBinding

class MatchAdapter (private val onItemClick: (MatchDataModel) -> Unit) : ListAdapter<MatchDataModel, MatchAdapter.ViewHolder> (
    object :DiffUtil.ItemCallback<MatchDataModel>() {
        override fun areItemsTheSame(oldItem: MatchDataModel, newItem: MatchDataModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MatchDataModel, newItem: MatchDataModel): Boolean {
            return oldItem == newItem
        }
    }
        ){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MatchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick)
    }

    class ViewHolder(private val binding: MatchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MatchDataModel, onItemClick: (MatchDataModel) -> Unit) = with(binding) {
            ivTeam.setImageResource(item.userImg)
            tvType.text = "팀 매칭"
            tvDetail.text = "${item.playerNum} : ${item.playerNum} ${item.gender}"
            tvViewCount.text = item.viewCount.toString()
            tvChatCount.text = item.applyCount.toString()
            tvSchedule.text = item.dateTime
            tvPlace.text = item.matchPlace

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

}