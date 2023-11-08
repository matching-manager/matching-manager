package com.example.matching_manager.ui.my.bookmark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.matching_manager.R
import com.example.matching_manager.databinding.MyRecruitItemBinding
import com.example.matching_manager.ui.team.TeamItem

class MyBookmarkRecruitListAdapter (private val onItemClick: (TeamItem.RecruitmentItem) -> Unit,
                                private val onDeleteClick: (TeamItem.RecruitmentItem) -> Unit) : ListAdapter<TeamItem.RecruitmentItem, MyBookmarkRecruitListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<TeamItem.RecruitmentItem>() {
        override fun areItemsTheSame(oldItem: TeamItem.RecruitmentItem, newItem: TeamItem.RecruitmentItem): Boolean {
            return oldItem.teamId == newItem.teamId
        }

        override fun areContentsTheSame(oldItem: TeamItem.RecruitmentItem, newItem: TeamItem.RecruitmentItem): Boolean {
            return oldItem == newItem
        }
    }
){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            MyRecruitItemBinding.inflate(
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

    class ViewHolder(private val binding: MyRecruitItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item : TeamItem.RecruitmentItem, onItemClick: (TeamItem.RecruitmentItem) -> Unit, onDeleteClick : (TeamItem.RecruitmentItem) -> Unit) = with(binding) {
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