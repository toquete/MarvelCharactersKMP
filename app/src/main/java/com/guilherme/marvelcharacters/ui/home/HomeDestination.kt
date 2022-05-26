package com.guilherme.marvelcharacters.ui.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.guilherme.marvelcharacters.navigation.TopLevelDestination

fun NavGraphBuilder.homeGraph(
    onDarkModeClick: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    composable(TopLevelDestination.HOME.route) {
        HomeRoute(
            onDarkModeClick = onDarkModeClick,
            onNavigateToDetail = onNavigateToDetail
        )
    }
}