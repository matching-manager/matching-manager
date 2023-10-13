package com.example.matching_manager.ui.home.arena

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.matching_manager.databinding.ArenaRecyclerviewItemBinding

class ArenaListAdapter(
    private val onClick: (ArenaModel) -> Unit
) : ListAdapter<ArenaModel, ArenaListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<ArenaModel>() {
        override fun areItemsTheSame(
            oldItem: ArenaModel,
            newItem: ArenaModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ArenaModel,
            newItem: ArenaModel
        ): Boolean {
            return oldItem.phone == newItem.phone
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ArenaRecyclerviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) { // binding
        val item = getItem(position) // ListAdapter의 메소드 getItem
        holder.bind(item)
    }

    class ViewHolder(
        private val binding: ArenaRecyclerviewItemBinding,
        private val onClick: (ArenaModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ArenaModel) = with(binding) {
            tvArenaTitle.text = item.placeName
            tvArenaAddress.text = item.roadAddressName

        }
    }

}