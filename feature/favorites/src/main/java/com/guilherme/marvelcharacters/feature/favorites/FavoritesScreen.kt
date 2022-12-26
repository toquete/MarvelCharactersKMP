package com.guilherme.marvelcharacters.feature.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.CharacterListItem
import kotlinx.coroutines.launch

@Composable
fun FavoritesRoute(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onCharacterClick: (Int) -> Unit
) {
    val viewModel: FavoritesViewModel = viewModel()
    val state by viewModel.uiState.collectAsState()

    FavoritesScreen(
        state = state,
        scaffoldState = scaffoldState,
        onCharacterClick = onCharacterClick,
        onSnackbarShown = viewModel::onSnackbarShown
    )
}

@Composable
internal fun FavoritesScreen(
    state: FavoritesUiState,
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onCharacterClick: (Int) -> Unit = {},
    onSnackbarShown: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        when (state) {
            is FavoritesUiState.ShowSnackbar -> {
                state.messageId?.let { id ->
                    val message = stringResource(id)
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(message)
                        onSnackbarShown()
                    }
                }
            }
            is FavoritesUiState.Success -> {
                LazyColumn {
                    items(state.list) { character ->
                        CharacterListItem(
                            name = character.name,
                            onCharacterClick = { onCharacterClick(character.id) }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoritesDropdownMenu(
    isActionButtonVisible: Boolean,
    onDeleteAllClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    if (isActionButtonVisible) {
        IconButton(onClick = { isMenuExpanded = true }) {
            Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
        }
        DropdownMenu(
            modifier = Modifier.width(128.dp),
            expanded = isMenuExpanded,
            onDismissRequest = { isMenuExpanded = false }
        ) {
            DropdownMenuItem(
                onClick = {
                    onDeleteAllClick()
                    isMenuExpanded = false
                }
            ) {
                Text(stringResource(R.string.delete_dialog_title))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesScreenPreview() {
    FavoritesScreen(
        state = FavoritesUiState.Success(
            list = listOf(
                Character(
                    id = 0,
                    name = "Spider-Man",
                    description = "",
                    thumbnail = ""
                )
            )
        )
    )
}