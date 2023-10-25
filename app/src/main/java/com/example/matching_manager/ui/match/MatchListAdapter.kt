package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.MatchItemBinding

class MatchListAdapter (private val onItemClick: (MatchDataModel) -> Unit) : ListAdapter<MatchDataModel, MatchListAdapter.ViewHolder> (
    object :DiffUtil.ItemCallback<MatchDataModel>() {
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

        fun bind(item : MatchDataModel, onItemClick: (MatchDataModel) -> Unit) = with(binding) {
            ivProfile.setImageResource(item.userImg)
            tvGame.text = item.game
            tvDetail.text = formatDetail(item.playerNum, item.gender)
            tvViewCount.text = item.viewCount.toString()
            tvChatCount.text = item.chatCount.toString()
            tvSchedule.text = item.schedule
            tvPlace.text = item.matchPlace

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun formatDetail(playerNum : Int, gender : String) : String {
            return "${playerNum}:${playerNum} ${gender}"
        }
    }

}