package com.example.podcastapp.presentation.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.podcastapp.databinding.FragmentDownloadsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DownloadsFragment : Fragment() {
    private var _binding: FragmentDownloadsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DownloadsViewModel by viewModel()
    private lateinit var downloadsAdapter: DownloadsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDownloadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        downloadsAdapter = DownloadsAdapter { episode ->

        }
        binding.rvDownloads.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = downloadsAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}