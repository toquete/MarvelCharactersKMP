package com.guilherme.marvelcharacters.feature.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.SnackbarMessageMP
import com.guilherme.marvelcharacters.feature.favorites.FavoritesScreen
import kotlinx.serialization.Serializable

@Serializable
object FavoritesRoute

fun NavController.navigateToFavorites(navOptions: NavOptions) {
    navigate(FavoritesRoute, navOptions)
}

fun NavGraphBuilder.favoritesScreen(
    onCharacterClick: (character: Character) -> Unit,
    onShowSnackbar: suspend (SnackbarMessageMP) -> Boolean
) {
    composable<FavoritesRoute> {
        FavoritesScreen(
            onCharacterClick = onCharacterClick,
            onShowSnackbar = onShowSnackbar
        )
    }
}