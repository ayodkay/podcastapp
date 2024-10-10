package com.example.podcastapp.presentation.podcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.podcastapp.R
import com.example.podcastapp.databinding.ItemEpisodeBinding
import com.example.podcastapp.domain.model.Episode
import java.text.SimpleDateFormat
import java.util.*

class EpisodeAdapter(
    private val onEpisodeClick: (Episode) -> Unit,
    private val onDownloadClick: (Episode) -> Unit,
    private val onFavoriteClick: (Episode) -> Unit
) : ListAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(EpisodeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val binding = ItemEpisodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EpisodeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class EpisodeViewHolder(private val binding: ItemEpisodeBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEpisodeClick(getItem(position))
                }
            }
            binding.btnDownload.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDownloadClick(getItem(position))
                }
            }
            binding.btnFavorite.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onFavoriteClick(getItem(position))
                }
            }
        }

        fun bind(episode: Episode) {
            binding.tvEpisodeTitle.text = episode.title
            binding.tvEpisodeDate.text = formatDate(episode.publicationDate)
            binding.tvEpisodeDuration.text = formatDuration(episode.duration)
            binding.btnDownload.setImageResource(
                if (episode.isDownloaded) R.drawable.ic_downloaded else R.drawable.ic_download
            )
            binding.btnFavorite.setImageResource(
                if (episode.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border
            )
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

    private class EpisodeDiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.guid == newItem.guid
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }
}