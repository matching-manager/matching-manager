package com.example.matching_manager.ui.team

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.TeamItemBinding

class TeamListAdapter() : ListAdapter<TeamModel, TeamListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<TeamModel>() {
        override fun areItemsTheSame(
            oldItem: TeamModel,
            newItem: TeamModel
        ): Boolean {
            return oldItem.teamName == newItem.teamName
        }

        override fun areContentsTheSame(
            oldItem: TeamModel,
            newItem: TeamModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TeamItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // binding
        val item = getItem(position) // ListAdapter의 메소드 getItem
        holder.bind(item)
    }
    class ViewHolder(
        private val binding: TeamItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TeamModel) = with(binding) {
            tvTeamName.text=item.teamName
//            tvAge.text=item.age
//            tvArea.text=item.area
//            tvGender.text=item.gender
//            tvLocation.text=item.location
//            tvMannerScore.text=item.mannerScore
//            tvPresidentName.text=item.presidentName
//            tvTeamMembers.text=item.teamMember
            //프로필 설정하기
//            ivProfile.
        }
    }
}