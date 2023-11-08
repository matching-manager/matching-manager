package com.example.matching_manager.ui.my.match

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyItemBinding
import com.example.matching_manager.ui.match.MatchDataModel

class MyMatchListAdapter (private val onItemClick: (MatchDataModel) -> Unit,
                          private val onMenuClick : (MatchDataModel) -> Unit) : ListAdapter<MatchDataModel, MyMatchListAdapter.ViewHolder>(
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
        holder.bind(item, onItemClick, onMenuClick)
    }

    class ViewHolder(private val binding: MyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MatchDataModel, onItemClick: (MatchDataModel) -> Unit, onMenuClick : (MatchDataModel) -> Unit) = with(binding) {
            ivProfile.load(item.userImg)
            tvGame.text = item.game
            tvDetail.text = formatDetail(item.playerNum, item.gender)
            tvSchedule.text = item.schedule
            tvPlace.text = item.matchPlace

            btnMenu.setOnClickListener {
                onMenuClick(item)
            }

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
            return "${playerNum}:${playerNum} ${gender}"
        }
    }

}