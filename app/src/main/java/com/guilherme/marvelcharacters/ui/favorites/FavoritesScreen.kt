package com.guilherme.marvelcharacters.ui.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.composethemeadapter.MdcTheme
import com.guilherme.marvelcharacters.R
import com.guilherme.marvelcharacters.domain.model.Character
import com.guilherme.marvelcharacters.ui.home.CharacterItem

@Composable
fun FavoritesRoute(
    viewModel: FavoritesViewModel = hiltViewModel(),
    onNavigateToDetail: (Int) -> Unit
) {
    val state by viewModel.state.collectAsState()
    FavoritesScreen(
        state,
        onItemClick = { character ->
            onNavigateToDetail(character.id)
        },
        onErrorMessageShown = viewModel::onErrorMessageShown,
        onDeleteAllClick = viewModel::onDeleteAllClick
    )
}

@Composable
fun FavoritesScreen(
    state: FavoritesState,
    onItemClick: (Character) -> Unit = {},
    onErrorMessageShown: () -> Unit = {},
    onDeleteAllClick: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(stringResource(R.string.favorites)) },
            actions = {
                FavoritesDropdownMenu(
                    isActionButtonVisible = state.list.isNotEmpty(),
                    onDeleteAllClick = { showDialog = true }
                )
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
                    snackbarHostState.showSnackbar(message)
                    onErrorMessageShown()
                }
            }
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState
            )
        }
    }
    FavoritesDeletionAlertDialog(
        isDialogVisible = showDialog,
        onDeleteButtonClick = {
            onDeleteAllClick()
            showDialog = false
        },
        onDismiss = { showDialog = false }
    )
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

@Composable
private fun FavoritesDeletionAlertDialog(
    isDialogVisible: Boolean,
    onDeleteButtonClick: () -> Unit,
    onDismiss: () -> Unit
) {
    if (isDialogVisible) {
        AlertDialog(
            title = { Text(stringResource(R.string.delete_dialog_title)) },
            text = { Text(stringResource(R.string.delete_dialog_message)) },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onDeleteButtonClick) {
                    Text(stringResource(R.string.delete).uppercase())
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(stringResource(R.string.cancel).uppercase())
                }
            }
        )
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
            )
        )
    }
}