package com.guilherme.marvelcharacters.feature.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.guilherme.marvelcharacters.core.ui.SnackbarMessage
import com.guilherme.marvelcharacters.feature.detail.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(val id: Int)

fun NavController.navigateToDetail(id: Int) {
    navigate(DetailRoute(id))
}

fun NavGraphBuilder.detailScreen(
    onNavigateUp: () -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean
) {
    composable<DetailRoute> {
        DetailScreen(
            onNavigateUp = onNavigateUp,
            onShowSnackbar = onShowSnackbar
        )
    }
}