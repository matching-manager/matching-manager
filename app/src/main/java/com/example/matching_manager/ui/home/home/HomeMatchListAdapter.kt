package com.example.matching_manager.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.HomeRecyclerviewItemMatchBinding
import com.example.matching_manager.ui.match.MatchDataModel

class HomeMatchListAdapter(
    private val onClick: (MatchDataModel) -> Unit
) : ListAdapter<MatchDataModel, HomeMatchListAdapter.ViewHolder>(
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
            HomeRecyclerviewItemMatchBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: HomeRecyclerviewItemMatchBinding,
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