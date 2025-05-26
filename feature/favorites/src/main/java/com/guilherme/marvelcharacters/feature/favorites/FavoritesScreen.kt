package com.guilherme.marvelcharacters.feature.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guilherme.marvelcharacters.core.model.Character
import com.guilherme.marvelcharacters.core.ui.theme.AppTheme
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
        onDeleteAllClick = viewModel::onDeleteAllClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FavoritesContent(
    state: FavoritesState,
    onCharacterClick: (character: Character) -> Unit = {},
    onDeleteAllClick: () -> Unit = {}
) {
    var isDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDropDownExpanded by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.favorites)) },
            actions = {
                if (state.characters.isEmpty()) return@TopAppBar
                IconButton(onClick = { isDropDownExpanded = !isDropDownExpanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = isDropDownExpanded,
                    onDismissRequest = { isDropDownExpanded = !isDropDownExpanded }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.delete_dialog_title)) },
                        onClick = {
                            isDialogOpen = !isDialogOpen
                            isDropDownExpanded = !isDropDownExpanded
                        }
                    )
                }
            }
        )
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
        if (isDialogOpen) {
            DeleteAllCharactersDialog(
                onDismissRequest = { isDialogOpen = false },
                onDeleteAllClick = {
                    onDeleteAllClick.invoke()
                    isDialogOpen = false
                }
            )
        }
    }
}

@Composable
private fun DeleteAllCharactersDialog(
    onDismissRequest: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onDeleteAllClick) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(stringResource(R.string.cancel))
            }
        },
        title = { Text(stringResource(R.string.delete_dialog_title)) },
        text = { Text(stringResource(R.string.delete_dialog_message)) }
    )
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

@Preview
@Composable
private fun DeleteAllCharactersDialogPreview() {
    AppTheme {
        DeleteAllCharactersDialog()
    }
}