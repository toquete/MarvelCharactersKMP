package com.guilherme.marvelcharacters

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.guilherme.marvelcharacters.core.ui.theme.AppTheme
import com.guilherme.marvelcharacters.feature.favorites.navigation.FavoritesRoute
import com.guilherme.marvelcharacters.feature.favorites.navigation.navigateToFavorites
import com.guilherme.marvelcharacters.feature.home.navigation.HomeRoute
import com.guilherme.marvelcharacters.feature.home.navigation.navigateToHome
import com.guilherme.marvelcharacters.navigation.AppNavHost
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val route: KClass<*>,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int
) {
    HOME(
        route = HomeRoute::class,
        selectedIcon = Icons.Default.EmojiEvents,
        unselectedIcon = Icons.Outlined.EmojiEvents,
        iconTextId = R.string.home,
        titleTextId = R.string.app_name
    ),
    FAVORITES(
        route = FavoritesRoute::class,
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Default.FavoriteBorder,
        iconTextId = R.string.favorites,
        titleTextId = R.string.favorites
    )
}

@Composable
fun App() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val currentTopLevelDestination = TopLevelDestination.entries.firstOrNull {
        currentDestination?.hasRoute(route = it.route) == true
    }
    AppContent(
        currentTopLevelDestination,
        currentDestination,
        navController
    )
}

@Composable
private fun AppContent(
    currentTopLevelDestination: TopLevelDestination?,
    currentDestination: NavDestination? = null,
    navController: NavHostController = rememberNavController()
) {
    val showAppBars = currentTopLevelDestination != null

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .fillMaxSize()
        ) {
            AppNavHost(
                navHostController = navController,
                modifier = Modifier.weight(1f)
            )

            if (showAppBars) {
                NavigationBar {
                    TopLevelDestination.entries.forEach { route ->
                        val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(route.route) } == true
                        NavigationBarItem(
                            selected = isSelected,
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) route.selectedIcon else route.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(route.iconTextId)) },
                            onClick = {
                                val navOptions = navOptions {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }

                                when (route) {
                                    TopLevelDestination.HOME -> navController.navigateToHome(navOptions)
                                    TopLevelDestination.FAVORITES -> navController.navigateToFavorites(navOptions)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    AppTheme {
        AppContent(TopLevelDestination.HOME)
    }
}