package com.guilherme.marvelcharacters.feature.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.SnackbarMessage
import com.guilherme.marvelcharacters.core.ui.theme.AppTheme
import com.guilherme.marvelcharacters.domain.model.FavoriteCharacter
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun DetailScreen(
    onNavigateUp: () -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
    viewModel: DetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    DetailContent(
        state = state,
        onNavigateUp = onNavigateUp,
        onFavoriteActionClick = viewModel::onFabClick,
        onUndoClick = viewModel::onUndoClick,
        onSnackbarShown = viewModel::onSnackbarShown,
        onShowSnackbar = onShowSnackbar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DetailContent(
    state: DetailState,
    onNavigateUp: () -> Unit = {},
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean = { _ -> false },
    onFavoriteActionClick: (isFavorite: Boolean) -> Unit = {},
    onUndoClick: () -> Unit = {},
    onSnackbarShown: () -> Unit = {}
) {
    LaunchedEffect(state.snackbarMessage) {
        state.snackbarMessage?.let {
            val isActionPerformed = onShowSnackbar.invoke(it)

            if (isActionPerformed) {
                onUndoClick.invoke()
            }

            onSnackbarShown.invoke()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(state.character?.character?.name.orEmpty()) },
            navigationIcon = {
                IconButton(onClick = onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        onFavoriteActionClick.invoke(state.character?.isFavorite ?: false)
                    }
                ) {
                    Icon(
                        imageVector = if (state.character?.isFavorite == true) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null
                    )
                }
            }
        )
        Text(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            text = state.character?.character?.description?.ifEmpty {
                stringResource(R.string.no_description_available)
            }.orEmpty()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailContentPreview() {
    AppTheme {
        DetailContent(
            state = DetailState(
                character = FavoriteCharacter(
                    character = Character(
                        id = 1,
                        name = "Spider-Man",
                        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                        thumbnail = ""
                    ),
                    isFavorite = false
                )
            )
        )
    }
}