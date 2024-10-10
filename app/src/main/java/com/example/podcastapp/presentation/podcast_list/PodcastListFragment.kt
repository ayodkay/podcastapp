package com.example.podcastapp.presentation.podcast_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.podcastapp.databinding.FragmentPodcastListBinding
import com.example.podcastapp.presentation.podcast.PodcastAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PodcastListFragment : Fragment() {

    private var _binding: FragmentPodcastListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PodcastListViewModel by viewModel()
    private lateinit var podcastAdapter: PodcastAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPodcastListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observePodcasts()
    }

    private fun setupRecyclerView() {
        podcastAdapter = PodcastAdapter { podcast ->
        }
        binding.rvPodcasts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = podcastAdapter
        }
    }

    private fun setupListeners() {
        binding.btnAddPodcast.setOnClickListener {
            val url = binding.etPodcastUrl.text.toString().trim()
            if (url.isNotEmpty()) {
                viewModel.addPodcast(url)
                binding.etPodcastUrl.text?.clear()
            }
        }
    }

    private fun observePodcasts() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.podcasts.collect { podcasts ->
                podcastAdapter.submitList(podcasts)
                updateEmptyState(podcasts.isEmpty())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.addPodcastStatus.collect { status ->
                when (status) {
                    is PodcastListViewModel.AddPodcastStatus.Success -> {
                        // Optionally show a success message
                    }
                    is PodcastListViewModel.AddPodcastStatus.Error -> {
                        // Show error message
                        showErrorMessage(status.message)
                    }
                    else -> { /* Loading state, if needed */ }
                }
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.rvPodcasts.visibility = View.GONE
        } else {
            binding.rvPodcasts.visibility = View.VISIBLE
        }
    }

    private fun showErrorMessage(message: String) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}