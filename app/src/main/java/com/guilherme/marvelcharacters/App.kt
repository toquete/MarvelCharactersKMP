package com.guilherme.marvelcharacters

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
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
    @StringRes val iconTextId: Int
) {
    HOME(
        route = HomeRoute::class,
        selectedIcon = Icons.Default.EmojiEvents,
        unselectedIcon = Icons.Outlined.EmojiEvents,
        iconTextId = R.string.home
    ),
    FAVORITES(
        route = FavoritesRoute::class,
        selectedIcon = Icons.Default.Favorite,
        unselectedIcon = Icons.Default.FavoriteBorder,
        iconTextId = R.string.favorites
    )
}

@Composable
fun App(onNightModeButtonClick: () -> Unit = {}) {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    val currentTopLevelDestination = TopLevelDestination.entries.firstOrNull {
        currentDestination?.hasRoute(route = it.route) == true
    }
    val snackbarHostState = remember { SnackbarHostState() }

    AppContent(
        currentTopLevelDestination,
        currentDestination,
        navController,
        snackbarHostState,
        onNightModeButtonClick
    )
}

@Composable
private fun AppContent(
    currentTopLevelDestination: TopLevelDestination?,
    currentDestination: NavDestination? = null,
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onNightModeButtonClick: () -> Unit = {}
) {
    val showAppBars = currentTopLevelDestination != null

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (!showAppBars) return@Scaffold
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
    ) { contentPadding ->
        AppNavHost(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize(),
            navHostController = navController,
            onNightModeButtonClick = onNightModeButtonClick,
            onShowSnackbar = { message ->
                snackbarHostState.showSnackbar(
                    message.text,
                    message.actionLabel,
                    message.withDismissAction,
                    message.duration
                ) == SnackbarResult.ActionPerformed
            }
        )
    }
}
