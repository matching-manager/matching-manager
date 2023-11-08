package com.example.matching_manager.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.HomeRecyclerviewItemAnnouncementBinding
import com.example.matching_manager.ui.match.MatchDataModel

// TODO : 임시로 MatchDataModel로 넣어뒀습니다!
// TODO : AnnouncementModel 구성 후에 변경 부탁드립니다~
class HomeAnnouncementListAdapter(
    private val onClick: (MatchDataModel) -> Unit
) : ListAdapter<MatchDataModel, HomeAnnouncementListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<MatchDataModel>() {
        override fun areItemsTheSame(
            oldItem: MatchDataModel,
            newItem: MatchDataModel
        ): Boolean {
            return oldItem.matchId == newItem.matchId
        }

        override fun areContentsTheSame(
            oldItem: MatchDataModel,
            newItem: MatchDataModel
        ): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeRecyclerviewItemAnnouncementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: HomeRecyclerviewItemAnnouncementBinding,
        private val onClick: (MatchDataModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MatchDataModel) = with(binding) {

            // TODO : item 화면에 뿌려주는 코드 넣어주세요

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}