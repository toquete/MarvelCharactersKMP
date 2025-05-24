package com.guilherme.marvelcharacters.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.feature.home.HomeScreen
import kotlinx.serialization.Serializable

@Serializable
object HomeRoute

fun NavController.navigateToHome() {
    navigate(HomeRoute)
}

fun NavGraphBuilder.homeScreen(onCharacterClick: (character: Character) -> Unit) {
    composable<HomeRoute> {
        HomeScreen(onCharacterClick = onCharacterClick)
    }
}