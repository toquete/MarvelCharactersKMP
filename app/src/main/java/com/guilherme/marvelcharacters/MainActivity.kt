package com.guilherme.marvelcharacters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.elevation.ElevationOverlayProvider
import com.guilherme.marvelcharacters.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private val mainDestinations = setOf(R.id.homeFragment, R.id.navigation_favorites)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment) as NavHostFragment
        navController = navHostFragment.navController

        setupNavigationListener()
        setupToolbar()
        setupBottomNavigationBar()
        setupNavigationBar()
    }

    private fun setupNavigationListener() {
        navController.addOnDestinationChangedListener { _, _, arguments ->
            with(binding) {
                val isMainDestination = arguments?.getBoolean("showAppBars", false) == true
                mainToolbar.isVisible = isMainDestination
                bottomNavigation.isVisible = isMainDestination
            }
        }
    }

    private fun setupBottomNavigationBar() {
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun setupToolbar() {
        val appBarConfiguration = AppBarConfiguration(mainDestinations)
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
