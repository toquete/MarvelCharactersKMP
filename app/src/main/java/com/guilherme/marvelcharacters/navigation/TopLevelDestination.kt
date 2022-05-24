package com.guilherme.marvelcharacters.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import com.guilherme.marvelcharacters.R

enum class TopLevelDestination(
    val route: String,
    @StringRes val titleId: Int,
    val icon: ImageVector
) {
    HOME(
        route = "home",
        titleId = R.string.home,
        icon = Icons.Filled.EmojiEvents
    ),
    FAVORITES(
        route = "favorites",
        titleId = R.string.favorites,
        icon = Icons.Filled.Favorite
    )
}

val TOP_LEVEL_DESTINATION_ROUTES = TopLevelDestination.values().map { it.route }
