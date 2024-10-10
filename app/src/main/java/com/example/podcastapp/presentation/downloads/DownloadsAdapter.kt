package com.example.podcastapp.presentation.downloads

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.podcastapp.data.mapper.toEntity
import com.example.podcastapp.databinding.ItemDownloadBinding
import com.example.podcastapp.domain.model.Episode
import java.text.SimpleDateFormat
import java.util.*

class DownloadsAdapter(private val onEpisodeClick: (Episode) -> Unit) :
    ListAdapter<Episode, DownloadsAdapter.DownloadViewHolder>(DownloadDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        val binding = ItemDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DownloadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DownloadViewHolder(private val binding: ItemDownloadBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEpisodeClick(getItem(position))
                }
            }
        }

        fun bind(episode: Episode) {
            binding.tvEpisodeTitle.text = episode.title
            binding.tvPodcastTitle.text = episode.toEntity().title
            binding.tvDownloadDate.text = formatDate(episode.publicationDate)
            binding.tvEpisodeDuration.text = formatDuration(episode.duration)
        }

        private fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }

        private fun formatDuration(durationInSeconds: Long): String {
            val hours = durationInSeconds / 3600
            val minutes = (durationInSeconds % 3600) / 60
            return if (hours > 0) {
                String.format("%d:%02d:%02d", hours, minutes, durationInSeconds % 60)
            } else {
                String.format("%d:%02d", minutes, durationInSeconds % 60)
            }
        }
    }

    private class DownloadDiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.guid == newItem.guid
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }
}