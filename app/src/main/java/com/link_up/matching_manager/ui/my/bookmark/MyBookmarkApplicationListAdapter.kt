package com.link_up.matching_manager.ui.my.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.MyApplicationItemBinding
import com.link_up.matching_manager.ui.team.TeamItem

class MyBookmarkApplicationListAdapter (private val onItemClick: (TeamItem.ApplicationItem) -> Unit,
                                    private val onDeleteClick : (TeamItem.ApplicationItem) -> Unit) : ListAdapter<TeamItem.ApplicationItem, MyBookmarkApplicationListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<TeamItem.ApplicationItem>() {
        override fun areItemsTheSame(oldItem: TeamItem.ApplicationItem, newItem: TeamItem.ApplicationItem): Boolean {
            return oldItem.teamId == newItem.teamId
        }

        override fun areContentsTheSame(oldItem: TeamItem.ApplicationItem, newItem: TeamItem.ApplicationItem): Boolean {
            return oldItem == newItem
        }
    }
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MyApplicationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClick, onDeleteClick)
    }

    class ViewHolder(private val binding: MyApplicationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : TeamItem.ApplicationItem, onItemClick: (TeamItem.ApplicationItem) -> Unit, onDeleteClick : (TeamItem.ApplicationItem) -> Unit) = with(binding) {
            ivProfile.load(item.userImg)
            tvType.text = item.type
            tvDetail.text = "${item.gender} ${item.playerNum}"
            tvSchedule.text = item.schedule
            tvPlace.text = item.area
            btnMenu.setImageResource(R.drawable.ic_trash)

            btnMenu.setOnClickListener {
                onDeleteClick(item)
            }

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }
    }
}