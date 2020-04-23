package com.guilherme.marvelcharacters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.guilherme.marvelcharacters.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.hostFragment)

        binding.bottomNavigation.setupWithNavController(navController)

        setupToolbar(navController)
    }

    private fun setupToolbar(navController: NavController) {
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.favoritesFragment))
        binding.mainToolbar.setupWithNavController(navController, appBarConfiguration)
    }
}
