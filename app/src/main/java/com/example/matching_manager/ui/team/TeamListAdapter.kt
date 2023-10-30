package com.example.matching_manager.ui.match

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.R
import com.example.matching_manager.databinding.TeamItemBinding
import com.example.matching_manager.databinding.TeamUnknownItemBinding
import com.example.matching_manager.ui.team.TeamItem

class TeamListAdapter(
    private val onClick: (TeamItem) -> Unit,
    private val onIncrementViewCount: (TeamItem) -> Unit,
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
                    onClick,
                    onIncrementViewCount
                )

            TeamItemViewType.Application.ordinal ->
                ApplicationViewHolder(
                    TeamItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    onClick,
                    onIncrementViewCount
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
        private val onClick: (TeamItem) -> Unit,
        private val onIncrementViewCount: (TeamItem) -> Unit,
    ) : ViewHolder(binding.root) {

        override fun onBind(item: TeamItem) = with(binding) {
            if (item is TeamItem.RecruitmentItem) {
                cvType.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.team_recruit_blue
                    )
                )
                ivProfile.setImageResource(item.teamProfile)
                tvType.text = item.type
                tvDetail.text = "${item.gender} ${item.playerNum}"
                tvSchedule.text = item.schedule
                tvPlace.text = item.area
                itemView.setOnClickListener {
                    onClick(item)
                    onIncrementViewCount(item) // 클릭 시 조회수 증가
                }
            }
        }
    }

    class ApplicationViewHolder(
        private val binding: TeamItemBinding,
        private val onClick: (TeamItem) -> Unit,
        private val onIncrementViewCount: (TeamItem) -> Unit,
    ) : ViewHolder(binding.root) {

        override fun onBind(item: TeamItem) = with(binding) {
            if (item is TeamItem.ApplicationItem) {
                cvType.setCardBackgroundColor(
                    ContextCompat.getColor(
                        itemView.context,
                        R.color.team_request_yellow
                    )
                )
                ivProfile.setImageResource(item.teamProfile)
                tvType.text = item.type
                tvDetail.text = "${item.gender} ${item.playerNum}"
                tvSchedule.text = item.schedule
                tvPlace.text = item.area
                //제목 넣어야함

                itemView.setOnClickListener {
                    onClick(item)
                    onIncrementViewCount(item) // 클릭 시 조회수 증가
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