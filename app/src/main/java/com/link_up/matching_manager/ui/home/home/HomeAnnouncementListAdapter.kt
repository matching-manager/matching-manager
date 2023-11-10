package com.link_up.matching_manager.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.link_up.matching_manager.databinding.HomeRecyclerviewItemAnnouncementBinding

class HomeAnnouncementListAdapter(
    private val onClick: (AnnouncementDataModel) -> Unit
) : ListAdapter<AnnouncementDataModel, HomeAnnouncementListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<AnnouncementDataModel>() {
        override fun areItemsTheSame(
            oldItem: AnnouncementDataModel,
            newItem: AnnouncementDataModel
        ): Boolean {
            return oldItem.announceNum == newItem.announceNum
        }

        override fun areContentsTheSame(
            oldItem: AnnouncementDataModel,
            newItem: AnnouncementDataModel
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
        private val onClick: (AnnouncementDataModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnnouncementDataModel) = with(binding) {
            tvAnnouncement.text = item.title

            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }
}