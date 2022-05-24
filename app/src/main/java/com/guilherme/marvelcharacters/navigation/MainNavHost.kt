package com.guilherme.marvelcharacters.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.guilherme.marvelcharacters.ui.detail.DetailDestination
import com.guilherme.marvelcharacters.ui.detail.DetailRoute
import com.guilherme.marvelcharacters.ui.favorites.FavoritesRoute
import com.guilherme.marvelcharacters.ui.home.HomeRoute

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = TopLevelDestination.HOME.route
) {
    NavHost(
        navController,
        startDestination,
        modifier
    ) {
        composable(TopLevelDestination.HOME.route) {
            HomeRoute(
                onNavigateToDetail = { navController.navigate("${DetailDestination.route}/$it") }
            )
        }
        composable(TopLevelDestination.FAVORITES.route) {
            FavoritesRoute(
                onNavigateToDetail = { navController.navigate("${DetailDestination.route}/$it") }
            )
        }
        composable(
            route = "${DetailDestination.route}/{${DetailDestination.characterIdArg}}",
            arguments = listOf(
                navArgument(DetailDestination.characterIdArg) { type = NavType.IntType }
            )
        ) {
            DetailRoute(onBackClick = navController::popBackStack)
        }
    }
}