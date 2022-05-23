package com.guilherme.marvelcharacters.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.material.composethemeadapter.MdcTheme
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.ui.home.CharacterItem

@Composable
fun FavoritesRoute(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val state by viewModel.state.collectAsState()
    FavoritesScreen(
        state,
        onItemClick = { character ->
            navController.navigate("detail/${character.id}")
        },
        onErrorMessageShown = viewModel::onErrorMessageShown,
        onActionButtonClick = {}
    )
}

@Composable
fun FavoritesScreen(
    state: FavoritesState,
    onItemClick: (Character) -> Unit,
    onErrorMessageShown: () -> Unit,
    onActionButtonClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.favorites)) },
            actions = {
                IconButton(onClick = onActionButtonClick) {
                    Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
                }
            }
        )
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.list) { character ->
                    CharacterItem(character, onItemClick = onItemClick)
                    Divider()
                }
            }
            state.messageId?.let { id ->
                val message = stringResource(id)
                LaunchedEffect(id) {
                    snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Long)
                    onErrorMessageShown()
                }
            }
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    MdcTheme {
        FavoritesScreen(
            state = FavoritesState(
                list = listOf(
                    Character(
                        id = 0,
                        name = "Spider-Man",
                        description = "Teste",
                        thumbnail = ""
                    ),
                    Character(
                        id = 0,
                        name = "Spider-Man",
                        description = "Teste",
                        thumbnail = ""
                    )
                )
            ),
            onItemClick = { },
            onErrorMessageShown = { },
            onActionButtonClick = { }
        )
    }
}