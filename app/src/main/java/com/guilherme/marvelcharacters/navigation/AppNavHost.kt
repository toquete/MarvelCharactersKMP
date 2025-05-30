package com.guilherme.marvelcharacters.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.guilherme.marvelcharacters.core.ui.SnackbarMessage
//import com.guilherme.marvelcharacters.feature.detail.navigation.detailScreen
//import com.guilherme.marvelcharacters.feature.detail.navigation.navigateToDetail
import com.guilherme.marvelcharacters.feature.favorites.navigation.favoritesScreen
import com.guilherme.marvelcharacters.feature.home.navigation.HomeRoute
import com.guilherme.marvelcharacters.feature.home.navigation.homeScreen

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
    modifier: Modifier = Modifier,
    onNightModeButtonClick: () -> Unit = {}
) {
    NavHost(
        navController = navHostController,
        startDestination = HomeRoute,
        modifier = modifier
    ) {
        homeScreen(
            onCharacterClick = { /*navHostController.navigateToDetail(it.id)*/ },
            onNightModeButtonClick = onNightModeButtonClick
        )
        favoritesScreen(
            onCharacterClick = { /*navHostController.navigateToDetail(it.id)*/ },
            onShowSnackbar = onShowSnackbar
        )
//        detailScreen(
//            onNavigateUp = { navHostController.navigateUp() },
//            onShowSnackbar = onShowSnackbar
//        )
    }
}