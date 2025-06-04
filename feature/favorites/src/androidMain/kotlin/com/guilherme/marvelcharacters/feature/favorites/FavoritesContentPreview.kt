package com.guilherme.marvelcharacters.feature.favorites

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.theme.AppTheme

@Preview(showBackground = true)
@Composable
fun FavoritesContentPreview() {
    AppTheme {
        FavoritesContent(
            state = FavoritesState(
                characters = listOf(
                    Character(
                        id = 1,
                        name = "Spider-Man",
                        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        thumbnail = ""
                    ),
                    Character(
                        id = 2,
                        name = "Hulk",
                        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        thumbnail = ""
                    )
                )
            )
        )
    }
}

@Preview
@Composable
private fun DeleteAllCharactersDialogPreview() {
    AppTheme {
        DeleteAllCharactersDialog()
    }
}