package com.guilherme.marvelcharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.guilherme.marvelcharacters.ui.theme.MarvelCharactersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MarvelCharactersTheme {
                val systemUiController = rememberSystemUiController()
                val useDarkIcons = isSystemInDarkTheme()
                val overlaidColor = LocalElevationOverlay.current?.apply(
                    color = if (useDarkIcons) MaterialTheme.colors.surface else MaterialTheme.colors.primary,
                    elevation = BottomNavigationDefaults.Elevation
                )

                MainScreen()
                SideEffect {
                    overlaidColor?.let {
                        systemUiController.setNavigationBarColor(color = overlaidColor)
                    }
                }
            }
        }
    }
}
