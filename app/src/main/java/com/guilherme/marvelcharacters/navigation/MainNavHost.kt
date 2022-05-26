package com.guilherme.marvelcharacters.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.guilherme.marvelcharacters.ui.detail.DetailDestination
import com.guilherme.marvelcharacters.ui.detail.detailGraph
import com.guilherme.marvelcharacters.ui.favorites.favoritesGraph
import com.guilherme.marvelcharacters.ui.home.homeGraph

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = TopLevelDestination.HOME.route,
    onDarkModeClick: () -> Unit = {}
) {
    NavHost(
        navController,
        startDestination,
        modifier
    ) {
        homeGraph(
            onDarkModeClick = onDarkModeClick,
            onNavigateToDetail = { navController.navigate("${DetailDestination.route}/$it") }
        )
        favoritesGraph {
            navController.navigate("${DetailDestination.route}/$it")
        }
        detailGraph {
            navController.popBackStack()
        }
    }
}