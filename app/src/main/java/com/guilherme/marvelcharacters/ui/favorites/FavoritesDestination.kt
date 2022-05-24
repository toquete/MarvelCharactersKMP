package com.guilherme.marvelcharacters.ui.favorites

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.guilherme.marvelcharacters.navigation.TopLevelDestination

fun NavGraphBuilder.favoritesGraph(
    onNavigateToDetail: (Int) -> Unit
) {
    composable(TopLevelDestination.FAVORITES.route) {
        FavoritesRoute {
            onNavigateToDetail(it)
        }
    }
}