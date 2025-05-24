package com.guilherme.marvelcharacters.feature.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.theme.AppTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun FavoritesScreen(
    viewModel: FavoritesViewModel = koinViewModel(),
    onCharacterClick: (character: Character) -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    FavoritesContent(
        state = state,
        onCharacterClick = onCharacterClick,
        onSnackbarShown = viewModel::onSnackbarShown
    )
}

@Composable
internal fun FavoritesContent(
    state: FavoritesState,
    onCharacterClick: (character: Character) -> Unit = {},
    onSnackbarShown: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val resources = LocalContext.current.resources
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            items(state.characters) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCharacterClick.invoke(it) }
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = it.name,
                        fontSize = 16.sp
                    )
                    HorizontalDivider()
                }
            }
        }
        LaunchedEffect(state.messageId) {
            scope.launch {
                state.messageId?.let {
                    snackbarHostState.showSnackbar(message = resources.getText(it).toString())
                }
                onSnackbarShown.invoke()
            }
        }
    }
}

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