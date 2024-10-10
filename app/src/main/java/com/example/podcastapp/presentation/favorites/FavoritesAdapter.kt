package com.example.podcastapp.presentation.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.podcastapp.R
import com.example.podcastapp.data.mapper.toEntity
import com.example.podcastapp.databinding.ItemFavoriteBinding
import com.example.podcastapp.domain.model.Episode
import java.text.SimpleDateFormat
import java.util.*

class FavoritesAdapter(
    private val onEpisodeClick: (Episode) -> Unit,
    private val onFavoriteClick: (Episode) -> Unit
) : ListAdapter<Episode, FavoritesAdapter.FavoriteViewHolder>(FavoriteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class FavoriteViewHolder(private val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEpisodeClick(getItem(position))
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
            binding.tvPodcastTitle.text = episode.toEntity().title
            binding.tvEpisodeDate.text = formatDate(episode.publicationDate)
            binding.tvEpisodeDuration.text = formatDuration(episode.duration)
            binding.btnFavorite.setImageResource(R.drawable.ic_favorite)
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

    private class FavoriteDiffCallback : DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem.guid == newItem.guid
        }

        override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean {
            return oldItem == newItem
        }
    }
}