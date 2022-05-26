package com.guilherme.marvelcharacters.ui.detail

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.ui.theme.MarvelCharactersTheme

@Composable
fun DetailRoute(
    viewModel: DetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    DetailScreen(
        state,
        onNavigationButtonClick = onBackClick,
        onMenuItemButtonClick = viewModel::onFabClick,
        onSnackbarShown = viewModel::onSnackbarShown,
        onSnackbarActionPerformed = viewModel::onUndoClick
    )
}

@Composable
fun DetailScreen(
    state: DetailComposeState,
    onNavigationButtonClick: () -> Unit = {},
    onMenuItemButtonClick: (Character) -> Unit = {},
    onSnackbarShown: () -> Unit = {},
    onSnackbarActionPerformed: (Character) -> Unit = {}
) {
    state.character?.let { character ->
        val scaffoldState = rememberScaffoldState()
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                TopAppBar(
                    title = { Text(character.name) },
                    navigationIcon = {
                        IconButton(onClick = onNavigationButtonClick) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { onMenuItemButtonClick(character) }) {
                            Icon(
                                imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Text(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                text = character.description.ifEmpty {
                    stringResource(R.string.no_description_available)
                }
            )
        }
        state.message?.let { snackbarMessage ->
            val message = stringResource(snackbarMessage.messageId)
            val actionLabel = stringResource(R.string.undo).uppercase()
            LaunchedEffect(snackbarMessage) {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = actionLabel.takeIf { snackbarMessage.showAction }
                ).also { result ->
                    if (result == SnackbarResult.ActionPerformed) {
                        onSnackbarActionPerformed(character)
                    }
                }
                onSnackbarShown()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    MarvelCharactersTheme {
        DetailScreen(
            state = DetailComposeState(
                character = Character(
                    id = 0,
                    name = "Spider-Man",
                    description = "The Amazing Spider-Man",
                    thumbnail = ""
                ),
                isFavorite = true
            )
        )
    }
}