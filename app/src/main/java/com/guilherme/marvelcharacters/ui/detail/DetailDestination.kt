package com.guilherme.marvelcharacters.ui.detail

import com.guilherme.marvelcharacters.navigation.Destination

object DetailDestination : Destination {
    override val route = "detail"
    const val characterIdArg = "characterId"
}