package com.example.podcastapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.podcastapp.databinding.FragmentFavoritesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoritesViewModel by viewModel()
    private lateinit var favoritesAdapter: FavoritesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(
            onEpisodeClick = { episode ->

            },
            onFavoriteClick = { episode ->

            }
        )
        binding.rvFavorites.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}