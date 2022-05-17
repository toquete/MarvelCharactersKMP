package com.guilherme.marvelcharacters.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.guilherme.marvelcharacters.R

sealed class Screen(val route: String, @StringRes val titleId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Filled.EmojiEvents)
    object Favorites : Screen("favorites", R.string.favorites, Icons.Filled.Favorite)
}
