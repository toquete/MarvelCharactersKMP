package com.guilherme.marvelcharacters

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.guilherme.marvelcharacters.core.ui.SnackbarManager
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
    val snackbarMessages by SnackbarManager.messages.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalContext.current.resources

    LaunchedEffect(snackbarMessages) {
        if (snackbarMessages.isNotEmpty()) {
            val message = snackbarMessages.first()
            val text = resources.getText(message.messageId)

            snackbarHostState.showSnackbar(
                message = text.toString(),
                duration = message.duration,
                withDismissAction = message.withDismissAction
            )

            SnackbarManager.setMessageShown(message.id)
        }
    }
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
        contentWindowInsets = WindowInsets(left = 0, top = 0, right = 0, bottom = 0),
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
            onNightModeButtonClick = onNightModeButtonClick
        )
    }
}
