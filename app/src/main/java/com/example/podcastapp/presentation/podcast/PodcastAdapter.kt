package com.example.podcastapp.presentation.podcast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.podcastapp.R
import com.example.podcastapp.databinding.ItemPodcastBinding
import com.example.podcastapp.domain.model.Podcast

class PodcastAdapter(private val onPodcastClick: (Podcast) -> Unit) :
    ListAdapter<Podcast, PodcastAdapter.PodcastViewHolder>(PodcastDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val binding = ItemPodcastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PodcastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PodcastViewHolder(private val binding: ItemPodcastBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onPodcastClick(getItem(position))
                }
            }
        }

        fun bind(podcast: Podcast) {
            binding.tvPodcastTitle.text = podcast.title
            binding.tvPodcastAuthor.text = podcast.author
            binding.ivPodcastThumbnail.load(podcast.imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_podcast_placeholder)
                error(R.drawable.ic_podcast_placeholder)
            }
        }
    }

    private class PodcastDiffCallback : DiffUtil.ItemCallback<Podcast>() {
        override fun areItemsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Podcast, newItem: Podcast): Boolean {
            return oldItem == newItem
        }
    }
}