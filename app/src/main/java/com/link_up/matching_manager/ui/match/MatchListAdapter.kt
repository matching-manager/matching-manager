package com.link_up.matching_manager.ui.match

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MatchItemBinding

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

        @SuppressLint("ResourceAsColor")
        fun bind(item : MatchDataModel, onItemClick: (MatchDataModel) -> Unit) = with(binding) {
            ivProfile.load(item.userImg.toUri())
            tvGame.text = item.game
            tvDetail.text = formatDetail(item.playerNum, item.gender)
            tvSchedule.text = item.schedule
            tvPlace.text = item.matchPlace

            itemView.setOnClickListener {
                onItemClick(item)
            }

            when (item.game) {
                "축구" -> {
                    cvType.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.match_soccer_pink))
                }
                "풋살" -> {
                    cvType.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.match_futsal_purple))
                }
                "농구" -> {
                    cvType.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.match_basketball_orange))
                }
                "배드민턴" -> {
                    cvType.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.match_badminton_brown))
                }
                "볼링" -> {
                    cvType.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.match_bowling_skyblue))
                }
                else -> {
                    cvType.setCardBackgroundColor(ContextCompat.getColor(itemView.context, R.color.black))
                }
            }
        }

        private fun formatDetail(playerNum : Int, gender : String) : String {
            return "$gender ${playerNum}:${playerNum}"
        }
    }

}