package com.example.podcastapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.podcastapp.databinding.ActivityMainBinding
import com.example.podcastapp.presentation.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigation.setupWithNavController(navController)

        setupObservers()

    }

    private fun updateUIForNetworkState(isAvailable: Boolean) {
        if (!isAvailable) {
            Snackbar.make(binding.root, R.string.offline_message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showOfflineDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.offline_mode)
            .setMessage(R.string.offline_mode_message)
            .setPositiveButton(R.string.go_to_downloads) { _, _ ->
                navigateToDownloads()
            }
            .setNegativeButton(R.string.dismiss, null)
            .show()
    }

    private fun navigateToDownloads() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.downloadsFragment)
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.networkState.collect { isAvailable ->
                updateUIForNetworkState(isAvailable)
            }
        }

        lifecycleScope.launch {
            viewModel.showOfflineDialog.collect { shouldShow ->
                if (shouldShow) {
                    showOfflineDialog()
                }
            }
        }
    }

}