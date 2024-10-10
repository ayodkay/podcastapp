package com.example.podcastapp.presentation.podcastdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.podcastapp.R
import com.example.podcastapp.databinding.FragmentPodcastDetailBinding
import com.example.podcastapp.domain.model.Episode
import com.example.podcastapp.presentation.podcast.EpisodeAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PodcastDetailFragment : Fragment() {

    private var _binding: FragmentPodcastDetailBinding? = null
    private val binding get() = _binding!!

    private val args: PodcastDetailFragmentArgs by navArgs()
    private val viewModel: PodcastDetailViewModel by viewModel { parametersOf(args.podcastUrl) }

    private lateinit var episodeAdapter: EpisodeAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPodcastDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observePodcastDetails()
    }

    private fun setupRecyclerView() {
        episodeAdapter = EpisodeAdapter(
            onEpisodeClick = { episode -> playEpisode(episode) },
            onDownloadClick = { episode -> downloadEpisode(episode) },
            onFavoriteClick = { episode -> toggleFavorite(episode) }
        )
        binding.rvEpisodes.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = episodeAdapter
        }
    }

    private fun observePodcastDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.podcastDetails.collectLatest { details ->
                binding.apply {
                    ivPodcastCover.load(details.imageUrl) {
                        crossfade(true)
                        placeholder(R.drawable.ic_podcast_placeholder)
                        error(R.drawable.ic_podcast_placeholder)
                    }
                    tvPodcastTitle.text = details.title
                    tvPodcastAuthor.text = details.author
                    tvPodcastDescription.text = details.description
                    episodeAdapter.submitList(details.episodes)
                }
            }
        }
    }

    private fun playEpisode(episode: Episode) {
    }

    private fun downloadEpisode(episode: Episode) {
        viewModel.downloadEpisode(episode)
    }

    private fun toggleFavorite(episode: Episode) {
        viewModel.toggleFavorite(episode)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}