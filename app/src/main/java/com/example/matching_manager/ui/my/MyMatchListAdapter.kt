package com.example.matching_manager.ui.my

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.MyItemBinding
import com.example.matching_manager.ui.match.MatchDataModel

class MyMatchListAdapter (private val onItemClick: (MatchDataModel) -> Unit) : ListAdapter<MatchDataModel, MyMatchListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MatchDataModel>() {
        override fun areItemsTheSame(oldItem: MatchDataModel, newItem: MatchDataModel): Boolean {
            return oldItem.matchId == newItem.matchId
        }

        override fun areContentsTheSame(oldItem: MatchDataModel, newItem: MatchDataModel): Boolean {
            return oldItem == newItem
        }
    }
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MyItemBinding.inflate(
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

    class ViewHolder(private val binding: MyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MatchDataModel, onItemClick: (MatchDataModel) -> Unit) = with(binding) {
            ivProfile.setImageResource(item.userImg)
            tvType.text = "팀 매칭"
            tvDetail.text = "${item.playerNum} : ${item.playerNum} ${item.gender}"
            tvViewCount.text = item.viewCount.toString()
            tvChatCount.text = item.chatCount.toString()
            tvSchedule.text = item.schedule
            tvPlace.text = item.matchPlace

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

}