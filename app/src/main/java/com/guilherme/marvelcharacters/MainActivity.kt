package com.guilherme.marvelcharacters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.elevation.ElevationOverlayProvider
import com.guilherme.marvelcharacters.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.hostFragment)

        setupToolbar(navController)
        setupBottomNavigationBar(navController)
        setupNavigationBar()
    }

    private fun setupBottomNavigationBar(navController: NavController) {
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupToolbar(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.favoritesFragment))
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
        setSupportActionBar(binding.mainToolbar)
    }

    private fun setupNavigationBar() {
        with(ElevationOverlayProvider(this)) {
            if (isThemeElevationOverlayEnabled) {
                window.navigationBarColor = compositeOverlayWithThemeSurfaceColorIfNeeded(binding.bottomNavigation.elevation)
            }
        }
    }
}
