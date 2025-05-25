package com.guilherme.marvelcharacters

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.guilherme.marvelcharacters.core.ui.theme.AppTheme
import com.guilherme.marvelcharacters.feature.home.NightModeViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val nightModeViewModel: NightModeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var nightMode: Int by mutableIntStateOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                nightModeViewModel.nightMode.onEach {
                    nightMode = it
                }.collect()
            }
        }

        enableEdgeToEdge()

        setContent {
            val isDarkTheme = shouldUseDarkTheme(nightMode)

            LaunchedEffect(isDarkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        Color.TRANSPARENT,
                        Color.TRANSPARENT
                    ) { isDarkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        DefaultLightScrim,
                        DefaultDarkScrim
                    ) { isDarkTheme }
                )
            }

            AppTheme(
                darkTheme = isDarkTheme,
                dynamicColor = false
            ) {
                App()
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(nighMode: Int): Boolean {
    return when (nighMode) {
        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> isSystemInDarkTheme()
        AppCompatDelegate.MODE_NIGHT_NO -> false
        AppCompatDelegate.MODE_NIGHT_YES -> true
        else -> false
    }
}

private val DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
private val DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
