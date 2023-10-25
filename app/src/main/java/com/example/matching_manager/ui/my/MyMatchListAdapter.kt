package com.example.matching_manager.ui.my

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.MyItemBinding

class MyMatchListAdapter (private val onItemClick: (MyMatchDataModel) -> Unit,
                          private val onEditClick : (MyMatchDataModel) -> Unit,
                          private val onRemoveClick : (MyMatchDataModel) -> Unit
                          ) : ListAdapter<MyMatchDataModel, MyMatchListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MyMatchDataModel>() {
        override fun areItemsTheSame(oldItem: MyMatchDataModel, newItem: MyMatchDataModel): Boolean {
            return oldItem.matchId == newItem.matchId
        }

        override fun areContentsTheSame(oldItem: MyMatchDataModel, newItem: MyMatchDataModel): Boolean {
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
        holder.bind(item, onItemClick, onEditClick, onRemoveClick)
    }

    class ViewHolder(private val binding: MyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : MyMatchDataModel, onItemClick: (MyMatchDataModel) -> Unit, onEditClick : (MyMatchDataModel) -> Unit,
                 onRemoveClick : (MyMatchDataModel) -> Unit) = with(binding) {
            ivProfile.setImageResource(item.userImg)
            tvType.text = "팀 매칭"
            tvDetail.text = "${item.playerNum} : ${item.playerNum} ${item.gender}"
            tvViewCount.text = item.viewCount.toString()
            tvChatCount.text = item.chatCount.toString()
            tvSchedule.text = item.schedule
            tvPlace.text = item.matchPlace

            btnEdit.setOnClickListener {
                onEditClick(item)
            }

            btnDelete.setOnClickListener {
                onRemoveClick(item)
            }

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }

}