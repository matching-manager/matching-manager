package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.link_up.matching_manager.R
import com.link_up.matching_manager.databinding.TeamItemBinding
import com.link_up.matching_manager.databinding.TeamUnknownItemBinding
import com.link_up.matching_manager.ui.team.TeamItem

class TeamListAdapter(
    private val onClick: (TeamItem) -> Unit,
) : ListAdapter<TeamItem, TeamListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<TeamItem>() {
        override fun areItemsTheSame(oldItem: TeamItem, newItem: TeamItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TeamItem, newItem: TeamItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    //enum class로 분리
    enum class TeamItemViewType {
        Recruit, Application
    }

    abstract class ViewHolder(
        root: View,
    ) : RecyclerView.ViewHolder(root) {

        abstract fun onBind(item: TeamItem)
    }

    override fun getItemViewType(position: Int): Int = when (getItem(position)) {
        is TeamItem.RecruitmentItem -> TeamItemViewType.Recruit.ordinal
        is TeamItem.ApplicationItem -> TeamItemViewType.Application.ordinal
        else -> {
            TeamItemViewType.Recruit.ordinal
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            TeamItemViewType.Recruit.ordinal ->
                RecruitViewHolder(
                    TeamItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onClick
                )

            TeamItemViewType.Application.ordinal ->
                ApplicationViewHolder(
                    TeamItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onClick
                )

            else -> UnknownViewHolder(
                TeamUnknownItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class RecruitViewHolder(
        private val binding: TeamItemBinding,
        private val onClick: (TeamItem) -> Unit
    ) : ViewHolder(binding.root) {

        override fun onBind(item: TeamItem) = with(binding) {
            if (item is TeamItem.RecruitmentItem) {
                cvType.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.team_recruit_blue
                    )
                )
                ivProfile.load(item.userImg)
                tvType.text = item.type
                tvDetail.text = "${item.gender} ${item.playerNum}명"
                tvSchedule.text = item.schedule
                tvPlace.text = item.area
                itemView.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    class ApplicationViewHolder(
        private val binding: TeamItemBinding,
        private val onClick: (TeamItem) -> Unit,
    ) : ViewHolder(binding.root) {

        override fun onBind(item: TeamItem) = with(binding) {
            if (item is TeamItem.ApplicationItem) {
                cvType.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.team_request_yellow
                    )
                )
                ivProfile.load(item.userImg)
                tvType.text = item.type
                tvDetail.text = "${item.gender} ${item.playerNum}명"
                tvSchedule.text = item.schedule
                tvPlace.text = item.area
                //제목 넣어야함

                itemView.setOnClickListener {
                    onClick(item)
                }
            }
        }
    }

    class UnknownViewHolder(
        binding: TeamUnknownItemBinding,
    ) : ViewHolder(binding.root) {

        override fun onBind(item: TeamItem) = Unit
    }


}