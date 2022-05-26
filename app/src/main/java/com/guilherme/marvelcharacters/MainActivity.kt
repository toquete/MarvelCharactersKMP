package com.guilherme.marvelcharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.guilherme.marvelcharacters.ui.home.NightModeViewModel
import com.guilherme.marvelcharacters.ui.theme.MarvelCharactersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val nightModeViewModel: NightModeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state by nightModeViewModel.nightMode.collectAsState()
            MarvelCharactersTheme(darkTheme = isDarkThemeEnabled(state)) {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = !MaterialTheme.colors.isLight
                val overlaidNavigationBarColor = LocalElevationOverlay.current?.apply(
                    color = if (useDarkIcons) MaterialTheme.colors.surface else MaterialTheme.colors.primary,
                    elevation = BottomNavigationDefaults.Elevation
                )
                val overlaidStatusBarColor = LocalElevationOverlay.current?.apply(
                    color = MaterialTheme.colors.primarySurface,
                    elevation = AppBarDefaults.TopAppBarElevation
                )

                MainScreen(nightModeViewModel)
                SideEffect {
                    overlaidNavigationBarColor?.let {
                        systemUiController.setNavigationBarColor(color = it)
                    }
                    overlaidStatusBarColor?.let {
                        systemUiController.setStatusBarColor(color = it)
                    }
                }
            }
        }
    }

    @Composable
    private fun isDarkThemeEnabled(state: Int): Boolean = when (state) {
        AppCompatDelegate.MODE_NIGHT_NO -> false
        AppCompatDelegate.MODE_NIGHT_YES -> true
        else -> isSystemInDarkTheme()
    }
}
