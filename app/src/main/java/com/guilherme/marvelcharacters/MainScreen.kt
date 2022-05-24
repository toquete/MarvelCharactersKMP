package com.guilherme.marvelcharacters

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.material.composethemeadapter.MdcTheme
import com.guilherme.marvelcharacters.navigation.TopLevelDestination
import com.guilherme.marvelcharacters.ui.detail.DetailRoute
import com.guilherme.marvelcharacters.ui.favorites.FavoritesRoute
import com.guilherme.marvelcharacters.ui.home.HomeRoute

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            MainBottomBar(
                onNavigationItemClick = { route ->
                    navController.navigate(route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                currentDestination = currentDestination
            )
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = TopLevelDestination.HOME.route, modifier = Modifier.padding(innerPadding)) {
            composable(TopLevelDestination.HOME.route) {
                HomeRoute(navController = navController)
            }
            composable(TopLevelDestination.FAVORITES.route) {
                FavoritesRoute(navController = navController)
            }
            composable(
                route = "detail/{characterId}",
                arguments = listOf(
                    navArgument("characterId") { type = NavType.IntType }
                )
            ) {
                DetailRoute(navController = navController)
            }
        }
    }
}

@Composable
fun MainBottomBar(
    onNavigationItemClick: (String) -> Unit,
    currentDestination: NavDestination?
) {
    BottomNavigation {
        TopLevelDestination.values().forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = null) },
                label = { Text(text = stringResource(screen.titleId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = { onNavigationItemClick(screen.route) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MdcTheme {
        MainScreen()
    }
}