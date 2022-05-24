package com.guilherme.marvelcharacters.ui.detail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.guilherme.marvelcharacters.navigation.Destination

object DetailDestination : Destination {
    override val route = "detail"
    const val characterIdArg = "characterId"
}

fun NavGraphBuilder.detailGraph(
    onBackClick: () -> Unit
) {
    composable(
        route = "${DetailDestination.route}/{${DetailDestination.characterIdArg}}",
        arguments = listOf(
            navArgument(DetailDestination.characterIdArg) { type = NavType.IntType }
        )
    ) {
        DetailRoute(onBackClick = onBackClick)
    }
}